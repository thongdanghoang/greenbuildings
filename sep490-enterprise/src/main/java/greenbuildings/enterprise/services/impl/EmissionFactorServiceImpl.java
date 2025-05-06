package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.enterprise.dtos.EmissionFactorCriteriaDTO;
import greenbuildings.enterprise.entities.*;
import greenbuildings.enterprise.enums.EmissionUnit;
import greenbuildings.enterprise.enums.ImportExcelType;
import greenbuildings.enterprise.repositories.*;
import greenbuildings.enterprise.services.EmissionFactorService;
import greenbuildings.enterprise.services.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
@Slf4j
public class EmissionFactorServiceImpl implements EmissionFactorService {
    
    private final EmissionFactorRepository emissionFactorRepository;
    private final FuelRepository fuelRepository;
    private final EmissionSourceRepository sourceRepository;
    private final EnergyConversionRepository energyConversionRepository;
    private final MinioService minioService;
    private final ExcelImportFileRepository excelImportFileRepository;

    
    @Override
    @Cacheable(value = "emissionFactors")
    public Set<EmissionFactorEntity> findAllAvailable() {
        return new HashSet<>(emissionFactorRepository.findAllByActiveIsTrueAndEmissionUnitDenominatorNotNullAndEmissionUnitNumeratorNotNull());
    }
    
    @Override
    public List<EmissionFactorEntity> findBySource(UUID sourceId) {
        return emissionFactorRepository.findBySourceId(sourceId);
    }
    
    @Override
    public Optional<EmissionFactorEntity> findById(UUID id) {
        return emissionFactorRepository.findById(id);
    }
    
    @Override
    public Page<EmissionFactorEntity> search(SearchCriteriaDTO<EmissionFactorCriteriaDTO> searchCriteria, Pageable pageable) {
        var emissionFactorIDs = emissionFactorRepository.findByName(
                searchCriteria.criteria().criteria(),
                pageable);
        // Fetch each entity individually using findAllById(UUID id)
        var results = emissionFactorIDs.stream()
                                       .map(emissionFactorRepository::findAllById) // Fetch for each ID
                                       .flatMap(List::stream) // Flatten the list of lists
                                       .collect(Collectors.toMap(EmissionFactorEntity::getId, Function.identity()));
        return emissionFactorIDs.map(results::get);
    }
    
    @CacheEvict(value = "emissionFactors", allEntries = true)
    public void delete(UUID id) {
        var emissionFactor = emissionFactorRepository.findById(id).orElseThrow();
        if (emissionFactor.isActive()) {
            emissionFactor.setActive(false);
        } else {
            if (emissionFactor.getEmissionUnitNumerator() == null
                || emissionFactor.getEmissionUnitDenominator() == null
                || emissionFactor.getSource() == null) {
                throw new BusinessException("emissionFactor.disabled");
            }
            if (emissionFactor.isDirectEmission()
                && emissionFactor.getEnergyConversion() != null) {
                throw new BusinessException("emissionFactor.disabled");
            }
            emissionFactor.setActive(true);
        }
        emissionFactorRepository.save(emissionFactor);
    }
    
    @Override
    public void createOrUpdate(EmissionFactorEntity factor) {
        if (factor.getValidFrom().isAfter(factor.getValidTo())) {
            throw new BusinessException("emissionFactor.date");
        }
        emissionFactorRepository.save(factor);
    }

    @Override
    public void importFactorFromExcel(MultipartFile file) {
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            // Kiểm tra dòng tiêu đề
            Row headerRow = sheet.getRow(0);
            if (headerRow == null
                    || !headerRow.getCell(0).getStringCellValue().trim().equals("Emisson Factor")
                    || !headerRow.getCell(1).getStringCellValue().trim().equals("Hệ số phát thải")
                    || !headerRow.getCell(2).getStringCellValue().trim().equals("排放因子")
                    || !headerRow.getCell(3).getStringCellValue().trim().equals("CO2")
                    || !headerRow.getCell(4).getStringCellValue().trim().equals("CH4")
                    || !headerRow.getCell(5).getStringCellValue().trim().equals("N20")
                    || !headerRow.getCell(6).getStringCellValue().trim().equals("Emission Unit Numerator")
                    || !headerRow.getCell(7).getStringCellValue().trim().equals("Emission Unit Denominator")
                    || !headerRow.getCell(8).getStringCellValue().trim().equals("Emission Source")
                    || !headerRow.getCell(9).getStringCellValue().trim().equals("Nguồn phát thải")
                    || !headerRow.getCell(10).getStringCellValue().trim().equals("排放源")
                    || !headerRow.getCell(11).getStringCellValue().trim().equals("Direct Emission")
                    || !headerRow.getCell(12).getStringCellValue().trim().equals("Fuel")
                    || !headerRow.getCell(13).getStringCellValue().trim().equals("Nhiên liệu")
                    || !headerRow.getCell(14).getStringCellValue().trim().equals("燃料")
                    || !headerRow.getCell(15).getStringCellValue().trim().equals("Conversion Value")
                    || !headerRow.getCell(16).getStringCellValue().trim().equals("Conversion Unit Numerator")
                    || !headerRow.getCell(17).getStringCellValue().trim().equals("Conversion Unit Denominator")) {
                workbook.close();
                throw new BusinessException("business.excel.invalidFormat");
            }

            int consecutiveNullRows = 0; // Counter for consecutive null rows
            DataFormatter formatter = new DataFormatter(); // Initialize formatter

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

                // Use DataFormatter for string fields
                String factorEN = row.getCell(0) != null ? formatter.formatCellValue(row.getCell(0)).trim() : "";
                String factorVN = row.getCell(1) != null ? formatter.formatCellValue(row.getCell(1)).trim() : "";
                String factorZH = row.getCell(2) != null ? formatter.formatCellValue(row.getCell(2)).trim() : "";

                BigDecimal co2 = BigDecimal.valueOf(row.getCell(3) != null && row.getCell(3).getCellType() == CellType.NUMERIC ? row.getCell(3).getNumericCellValue() : 0.0);
                BigDecimal ch4 = BigDecimal.valueOf(row.getCell(4) != null && row.getCell(4).getCellType() == CellType.NUMERIC ? row.getCell(4).getNumericCellValue() : 0.0);
                BigDecimal n2o = BigDecimal.valueOf(row.getCell(5) != null && row.getCell(5).getCellType() == CellType.NUMERIC ? row.getCell(5).getNumericCellValue() : 0.0);

                // Validate and parse EmissionUnit for numerator and denominator
                EmissionUnit unitNumerator;
                EmissionUnit unitDenominator;
                try {
                    String numeratorValue = row.getCell(6) != null ? formatter.formatCellValue(row.getCell(6)) : "";
                    unitNumerator = numeratorValue.isEmpty() ? null : EmissionUnit.valueOf(numeratorValue);
                } catch (IllegalArgumentException e) {
                    throw new BusinessException("business.excel.invalidEmissionUnit", "Invalid Emission Unit Numerator at row " + (i + 1));
                }

                try {
                    String denominatorValue = row.getCell(7) != null ? formatter.formatCellValue(row.getCell(7)) : "";
                    unitDenominator = denominatorValue.isEmpty() ? null : EmissionUnit.valueOf(denominatorValue);
                } catch (IllegalArgumentException e) {
                    throw new BusinessException("business.excel.invalidEmissionUnit", "Invalid Emission Unit Denominator at row " + (i + 1));
                }

                String sourceEN = row.getCell(8) != null ? formatter.formatCellValue(row.getCell(8)).trim() : "";
                String sourceVN = row.getCell(9) != null ? formatter.formatCellValue(row.getCell(9)).trim() : "";
                String sourceCN = row.getCell(10) != null ? formatter.formatCellValue(row.getCell(10)).trim() : "";
                String directEmission = row.getCell(11) != null ? formatter.formatCellValue(row.getCell(11)) : "";

                String fuelEN = row.getCell(12) != null ? formatter.formatCellValue(row.getCell(12)).trim() : "";
                String fuelVN = row.getCell(13) != null ? formatter.formatCellValue(row.getCell(13)).trim() : "";
                String fuelCN = row.getCell(14) != null ? formatter.formatCellValue(row.getCell(14)).trim() : "";
                BigDecimal conversionValue = BigDecimal.valueOf(row.getCell(15) != null && row.getCell(15).getCellType() == CellType.NUMERIC ? row.getCell(15).getNumericCellValue() : 0.0);

                EmissionUnit unitNumeratorConversion;
                EmissionUnit unitDenominatorConversion;
                try {
                    String numeratorValueConversion = row.getCell(16) != null ? formatter.formatCellValue(row.getCell(16)) : "";
                    unitNumeratorConversion = numeratorValueConversion.isEmpty() ? null : EmissionUnit.valueOf(numeratorValueConversion);
                } catch (IllegalArgumentException e) {
                    throw new BusinessException("business.excel.invalidEmissionUnit", "Invalid Conversion Unit Numerator at row " + (i + 1));
                }

                try {
                    String denominatorValueConversion = row.getCell(17) != null ? formatter.formatCellValue(row.getCell(17)) : "";
                    unitDenominatorConversion = denominatorValueConversion.isEmpty() ? null : EmissionUnit.valueOf(denominatorValueConversion);
                } catch (IllegalArgumentException e) {
                    throw new BusinessException("business.excel.invalidEmissionUnit", "Invalid Conversion Unit Denominator at row " + (i + 1));
                }
                assert unitNumerator != null;
                assert unitDenominator != null;
                assert unitDenominatorConversion != null;
                assert unitNumeratorConversion != null;
                if (!fuelVN.isEmpty() || !fuelEN.isEmpty() || !fuelCN.isEmpty()) {
                    boolean exists = energyConversionRepository.existsByName(fuelVN, fuelEN, fuelCN);
                    if (!exists) {
                        // Create and populate the FuelEntity
                        FuelEntity fuel = new FuelEntity();
                        fuel.setNameVN(fuelVN);
                        fuel.setNameEN(fuelEN);
                        fuel.setNameZH(fuelCN);
                        fuelRepository.save(fuel);
                        // Create and populate the EnergyConversionEntity
                        EnergyConversionEntity energyConversion = new EnergyConversionEntity();
                        energyConversion.setConversionValue(conversionValue);
                        energyConversion.setConversionUnitNumerator(unitNumeratorConversion);
                        energyConversion.setConversionUnitDenominator(unitDenominatorConversion);
                        energyConversion.setFuel(fuel);
                        energyConversionRepository.save(energyConversion);
                    }else{
                        // Update the existing EnergyConversionEntity
                        Optional<EnergyConversionEntity> energyConversion = energyConversionRepository.findByName(fuelVN, fuelEN, fuelCN);
                        if (energyConversion.isPresent()) {

                            energyConversion.get().setConversionValue(conversionValue);
                            energyConversion.get().setConversionUnitNumerator(unitNumeratorConversion);
                            energyConversion.get().setConversionUnitDenominator(unitDenominatorConversion);
                            energyConversionRepository.save(energyConversion.get());
                            boolean existSource = sourceRepository.existsByName(sourceVN, sourceEN, sourceCN);
                            if (existSource) {
                                 boolean existEmissionFactor = emissionFactorRepository.existsByEmissionFactor(
                                                factorVN, factorEN, factorZH,
                                                co2, ch4, n2o,
                                                unitNumerator.name(), unitDenominator.name());
                                if (existEmissionFactor) {
                                    continue;
                                }
                            }


                        }
                    }
                }

                Optional<EnergyConversionEntity> energyConversion = energyConversionRepository.findByName(fuelVN, fuelEN, fuelCN);
                if (energyConversion.isEmpty()) {
                    continue; // Skip if already exists
                }

                emissionSourceCheck(sourceVN, sourceEN, sourceCN);
                EmissionSourceEntity source = sourceRepository.findByName(sourceVN, sourceEN, sourceCN);
                if (source == null) {
                    continue; // Skip if already exists
                }


                // Create and populate the EmissionFactorEntity
                EmissionFactorEntity emissionFactor = new EmissionFactorEntity();
                emissionFactor.setNameEN(factorEN);
                emissionFactor.setNameVN(factorVN);
                emissionFactor.setNameZH(factorZH);
                emissionFactor.setCo2(co2);
                emissionFactor.setCh4(ch4);
                emissionFactor.setN2o(n2o);
                emissionFactor.setEmissionUnitNumerator(unitNumerator);
                emissionFactor.setEmissionUnitDenominator(unitDenominator);
                emissionFactor.setSource(source);
                emissionFactor.setDirectEmission(directEmission.equalsIgnoreCase("Yes"));

                // Set the parsed dates in the EmissionFactorEntity
                emissionFactor.setValidFrom(LocalDateTime.now());
                emissionFactor.setValidTo(LocalDateTime.now());

                emissionFactor.setEnergyConversion(energyConversion.get());

                // Save emissionFactor (assuming you have a repository for it)
                emissionFactorRepository.save(emissionFactor);
            }

            workbook.close();
        } catch (IOException e) {
            throw new BusinessException("business.excel.ioError", e.getMessage());
        }
    }

    private void emissionSourceCheck(String sourceVN, String sourceEN, String sourceCN) {
        if (!sourceVN.isEmpty() || !sourceEN.isEmpty() || !sourceCN.isEmpty()) {
            boolean exists = sourceRepository.existsByName(sourceVN, sourceEN, sourceCN);
            if (!exists) {
                // Create and populate the EmissionSourceEntity
                EmissionSourceEntity source = new EmissionSourceEntity();
                source.setNameVN(sourceVN);
                source.setNameEN(sourceEN);
                source.setNameZH(sourceCN);
                sourceRepository.save(source);
            }
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
            var existingFiles = excelImportFileRepository.findByType(ImportExcelType.EMISSION_FACTOR).orElse(null);
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
            excelImportFile.setType(ImportExcelType.EMISSION_FACTOR);
            excelImportFile.setMinioPath(minioService.uploadFile(file, excelId));
            excelImportFileRepository.save(excelImportFile);
        } catch (Exception e) {
            throw new TechnicalException("Failed to upload Excel file to MinIO", e);
        }
    }

    @Override
    public ExcelImportFileEntity getExcelImportFile() {
        return (ExcelImportFileEntity) excelImportFileRepository.findByType(ImportExcelType.EMISSION_FACTOR).stream().findFirst()
                .orElseThrow(() -> new BusinessException("business.excel.notFound"));
    }
}
