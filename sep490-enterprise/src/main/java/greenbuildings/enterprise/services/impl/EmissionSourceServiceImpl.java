package greenbuildings.enterprise.services.impl;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.enterprise.dtos.EmissionSourceCriteriaDTO;

import greenbuildings.enterprise.entities.EmissionSourceEntity;
import greenbuildings.enterprise.entities.ExcelImportFileEntity;
import greenbuildings.enterprise.enums.ImportExcelType;
import greenbuildings.enterprise.repositories.EmissionSourceRepository;
import greenbuildings.enterprise.repositories.ExcelImportFileRepository;
import greenbuildings.enterprise.services.EmissionSourceService;
import greenbuildings.enterprise.services.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Throwable.class)
@Slf4j
@RequiredArgsConstructor
public class EmissionSourceServiceImpl implements EmissionSourceService {
    
    private final EmissionSourceRepository emissionSourceRepository;
    private final MinioService minioService;
    private final ExcelImportFileRepository excelImportFileRepository;
    
    @Override
    @Cacheable("emissionSources")
    public Set<EmissionSourceEntity> findAll() {
        return new HashSet<>(emissionSourceRepository.findAll());
    }

    @Override
    public Page<EmissionSourceEntity> search(SearchCriteriaDTO<EmissionSourceCriteriaDTO> searchCriteria, Pageable pageable) {
        var emissionSourceIDs = emissionSourceRepository.findByName(
                searchCriteria.criteria().criteria(),
               pageable);
        var results = emissionSourceRepository.findAllById(emissionSourceIDs.toSet())
                .stream()
                .collect(Collectors.toMap(EmissionSourceEntity::getId, Function.identity()));
        return emissionSourceIDs.map(results::get);
    }

    @Override
    public void createOrUpdate(EmissionSourceEntity emissionSourceEntity) {
        emissionSourceRepository.save(emissionSourceEntity);
    }

    @Override
    public Optional<EmissionSourceEntity> findById(UUID id) {
        return emissionSourceRepository.findById(id);
    }

    @Override
    public void importFromExcel(MultipartFile file) {
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            // Kiểm tra dòng tiêu đề
            Row headerRow = sheet.getRow(0);
            if (headerRow == null
                    || !headerRow.getCell(0).getStringCellValue().trim().equals("Nguồn phát thải")
                    || !headerRow.getCell(1).getStringCellValue().trim().equals("Emission source")
                    || !headerRow.getCell(2).getStringCellValue().trim().equals("排放源")) {
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

                String vn = row.getCell(0)!= null ? row.getCell(0).getStringCellValue().trim() : "";
                String en = row.getCell(1)!= null ? row.getCell(1).getStringCellValue().trim() : "";
                String cn = row.getCell(2)!= null ? row.getCell(2).getStringCellValue().trim() : "";

                if (!vn.isEmpty() || !en.isEmpty() || !cn.isEmpty()) {
                    boolean exists = emissionSourceRepository.existsByName(vn, en, cn);
                    if (exists) {
                        continue; // Bỏ qua nếu đã tồn tại
                    }
                }


                EmissionSourceEntity source = new EmissionSourceEntity();
                source.setNameVN(vn);
                source.setNameEN(en);
                source.setNameZH(cn);

                emissionSourceRepository.save(source);
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
            var existingFiles = excelImportFileRepository.findByType(ImportExcelType.EMISSION_SOURCE).orElse(null);
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
            excelImportFile.setType(ImportExcelType.EMISSION_SOURCE);
            excelImportFile.setMinioPath(minioService.uploadFile(file, excelId));
            excelImportFileRepository.save(excelImportFile);
        } catch (Exception e) {
            throw new TechnicalException("Failed to upload Excel file to MinIO", e);
        }
    }

    @Override
    public ExcelImportFileEntity getExcelImportFile() {
        return (ExcelImportFileEntity) excelImportFileRepository.findByType(ImportExcelType.EMISSION_SOURCE).stream().findFirst()
                .orElseThrow(() -> new BusinessException("business.excel.notFound"));
    }

}
