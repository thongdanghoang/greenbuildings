package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.enterprise.dtos.FuelCriteriaDTO;
import greenbuildings.enterprise.entities.EnergyConversionEntity;
import greenbuildings.enterprise.entities.ExcelImportFileEntity;
import greenbuildings.enterprise.entities.FuelEntity;
import greenbuildings.enterprise.enums.EmissionUnit;
import greenbuildings.enterprise.enums.ImportExcelType;
import greenbuildings.enterprise.repositories.EnergyConversionRepository;
import greenbuildings.enterprise.repositories.ExcelImportFileRepository;
import greenbuildings.enterprise.repositories.FuelRepository;
import greenbuildings.enterprise.services.EnergyConversionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class EnergyConversionServiceImpl implements EnergyConversionService {

    private final EnergyConversionRepository energyConversionRepository;
    private final FuelRepository fuelRepository;
    private final MinioService minioService;
    private final ExcelImportFileRepository excelImportFileRepository;

    @Override
    public List<EnergyConversionEntity> findAll() {
        return energyConversionRepository.findAll();
    }

    @Override
    public Page<EnergyConversionEntity> search(SearchCriteriaDTO<FuelCriteriaDTO> searchCriteria, Pageable pageable) {
        var fuelIDs = energyConversionRepository.findByName(
                searchCriteria.criteria().criteria(),
                pageable);
        var results = energyConversionRepository.findAllById(fuelIDs.toSet())
                .stream()
                .collect(Collectors.toMap(EnergyConversionEntity::getId, Function.identity()));
        return fuelIDs.map(results::get);
    }

    @Override
    public void createOrUpdate(EnergyConversionEntity energyConversionEntity) {
        if (energyConversionEntity.getId() == null) {
            FuelEntity fuelEntity = new FuelEntity();
            fuelEntity.setNameEN(energyConversionEntity.getFuel().getNameEN());
            fuelEntity.setNameZH(energyConversionEntity.getFuel().getNameZH());
            fuelEntity.setNameVN(energyConversionEntity.getFuel().getNameVN());
            FuelEntity fuelEntity1 = fuelRepository.save(fuelEntity);
            energyConversionEntity.setFuel(fuelEntity1);
        } else {
            FuelEntity fuelOld = fuelRepository.findById(energyConversionEntity.getFuel().getId()).get();
            fuelOld.setNameEN(energyConversionEntity.getFuel().getNameEN());
            fuelOld.setNameZH(energyConversionEntity.getFuel().getNameZH());
            fuelOld.setNameVN(energyConversionEntity.getFuel().getNameVN());
            FuelEntity fuelEntity = fuelRepository.save(fuelOld);
            energyConversionEntity.setFuel(fuelEntity);
        }
        energyConversionRepository.save(energyConversionEntity);
    }

    @Override
    public Optional<EnergyConversionEntity> findById(UUID id) {
        return energyConversionRepository.findById(id);
    }


    @Override
    public void importFuelFromExcel(MultipartFile file) {
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            // Kiểm tra dòng tiêu đề
            Row headerRow = sheet.getRow(0);
            if (headerRow == null
                    || !headerRow.getCell(0).getStringCellValue().trim().equals("Fuel")
                    || !headerRow.getCell(1).getStringCellValue().trim().equals("Nhiên liệu")
                    || !headerRow.getCell(2).getStringCellValue().trim().equals("燃料")
                    || !headerRow.getCell(3).getStringCellValue().trim().equals("Conversion Value")
                    || !headerRow.getCell(4).getStringCellValue().trim().equals("Conversion Unit Numerator")
                    || !headerRow.getCell(5).getStringCellValue().trim().equals("Conversion Unit Denominator")) {
                workbook.close();
                throw new BusinessException("business.excel.invalidFormat");
            }

            int consecutiveNullRows = 0; // Counter for consecutive null rows

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                // Check if row is null or empty
                if (row == null || isRowEmpty(row)) {
                    consecutiveNullRows++;
                    if (consecutiveNullRows >= 2) {
                        break; // Exit loop if 2 consecutive null rows are found
                    }
                    continue;
                }

                // Reset counter if a non-null row is found
                consecutiveNullRows = 0;

                // Extract data from each cell (6 columns total):
                // Column 0: Fuel (VN), Column 1: Fuel (EN), Column 2: Fuel (CN),
                // Column 3: Conversion Value, Column 4: Conversion Unit Numerator,
                // Column 5: Conversion Unit Denominator
                String fuelVN = row.getCell(0) != null ? row.getCell(0).getStringCellValue().trim() : "";
                String fuelEN = row.getCell(1) != null ? row.getCell(1).getStringCellValue().trim(): "";
                String fuelCN = row.getCell(2) != null ? row.getCell(2).getStringCellValue().trim() : "";
                BigDecimal conversionValue = BigDecimal.valueOf(row.getCell(3) != null ? row.getCell(3).getNumericCellValue() : 0.0);

                // Validate and parse EmissionUnit for numerator and denominator
                EmissionUnit unitNumerator;
                EmissionUnit unitDenominator;
                try {
                    String numeratorValue = row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "";
                    unitNumerator = numeratorValue.isEmpty() ? null : EmissionUnit.valueOf(numeratorValue);
                } catch (IllegalArgumentException e) {
                    throw new BusinessException("business.excel.invalidEmissionUnit", "Invalid Conversion Unit Numerator at row " + (i + 1));
                }

                try {
                    String denominatorValue = row.getCell(5) != null ? row.getCell(5).getStringCellValue() : "";
                    unitDenominator = denominatorValue.isEmpty() ? null : EmissionUnit.valueOf(denominatorValue);
                } catch (IllegalArgumentException e) {
                    throw new BusinessException("business.excel.invalidEmissionUnit", "Invalid Conversion Unit Denominator at row " + (i + 1));
                }

                if (!fuelVN.isEmpty() || !fuelEN.isEmpty() || !fuelCN.isEmpty()) {
                    boolean exists = energyConversionRepository.existsByName(fuelVN, fuelEN, fuelCN);
                    if (exists) {
                        Optional<EnergyConversionEntity> energyConversion = energyConversionRepository.findByName(fuelVN, fuelEN, fuelCN);
                        if (energyConversion.isPresent()) {
                            energyConversion.get().setConversionValue(conversionValue);
                            energyConversion.get().setConversionUnitNumerator(unitNumerator);
                            energyConversion.get().setConversionUnitDenominator(unitDenominator);
                            energyConversionRepository.save(energyConversion.get());
                        }
                        continue; // Skip if already exists
                    }
                }


                // Create and populate the FuelEntity
                FuelEntity fuel = new FuelEntity();
                fuel.setNameVN(fuelVN);
                fuel.setNameEN(fuelEN);
                fuel.setNameZH(fuelCN);
                fuelRepository.save(fuel);

                // Create and populate the EnergyConversionEntity
                EnergyConversionEntity energyConversion = new EnergyConversionEntity();
                energyConversion.setConversionValue(conversionValue);
                energyConversion.setConversionUnitNumerator(unitNumerator);
                energyConversion.setConversionUnitDenominator(unitDenominator);
                energyConversion.setFuel(fuel);
                energyConversionRepository.save(energyConversion);
            }

            workbook.close();
        } catch (IOException ignored) {
        }
    }

    // Helper method to check if a row is empty
    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK && !cell.toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void uploadExcelToMinio(MultipartFile file) {
        try {
            var existingFiles = excelImportFileRepository.findByType(ImportExcelType.FUEL_CONVERSION).orElse(null);
            if(Objects.nonNull(existingFiles)) {
                throw new BusinessException("business.excel.exist");
            }
            // Generate a unique record ID for the upload
            String excelId = UUID.randomUUID().toString();
            // Create a new ExcelImportFileEntity
            ExcelImportFileEntity excelImportFile = new ExcelImportFileEntity();
            excelImportFile.setFileName(file.getOriginalFilename());
            excelImportFile.setContentType(file.getContentType());
            excelImportFile.setFileSize(file.getSize());
            excelImportFile.setUploadDate(LocalDateTime.now());
            excelImportFile.setType(ImportExcelType.FUEL_CONVERSION);
            excelImportFile.setMinioPath(minioService.uploadFile(file, excelId));
            excelImportFileRepository.save(excelImportFile);
        } catch (Exception e) {
            throw new TechnicalException("Failed to upload Excel file to MinIO", e);
        }
    }

    @Override
    public ExcelImportFileEntity getExcelImportFile() {
        return (ExcelImportFileEntity) excelImportFileRepository.findByType(ImportExcelType.FUEL_CONVERSION).stream().findFirst()
                .orElseThrow(() -> new BusinessException("business.excel.notFound"));
    }
    
    @Override
    public EnergyConversionEntity findByFactorId(UUID id) {
        return energyConversionRepository.findByFactorId(id).orElse(null);
    }
}

