package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionSourceCriteriaDTO;
import greenbuildings.enterprise.entities.EmissionSourceEntity;
import greenbuildings.enterprise.entities.ExcelImportFileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface EmissionSourceService {
    Set<EmissionSourceEntity> findAll();
    Page<EmissionSourceEntity> search(SearchCriteriaDTO<EmissionSourceCriteriaDTO> searchCriteria, Pageable pageable);
    void createOrUpdate(EmissionSourceEntity emissionSourceEntity);
    Optional<EmissionSourceEntity> findById(UUID id);
    void importFromExcel(MultipartFile file);
    void uploadExcelToMinio(MultipartFile file);
    ExcelImportFileEntity getExcelImportFile();
}
