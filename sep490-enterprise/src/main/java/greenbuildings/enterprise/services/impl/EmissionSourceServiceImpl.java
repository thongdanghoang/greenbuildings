package greenbuildings.enterprise.services.impl;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.dtos.EmissionSourceCriteriaDTO;

import greenbuildings.enterprise.entities.EmissionSourceEntity;
import greenbuildings.enterprise.repositories.EmissionSourceRepository;
import greenbuildings.enterprise.services.EmissionSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Throwable.class)
@Slf4j
@RequiredArgsConstructor
public class EmissionSourceServiceImpl implements EmissionSourceService {
    
    private final EmissionSourceRepository emissionSourceRepository;
    
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

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String vn = row.getCell(0).getStringCellValue();
            String en = row.getCell(1).getStringCellValue();
            String cn = row.getCell(2).getStringCellValue();

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
}
