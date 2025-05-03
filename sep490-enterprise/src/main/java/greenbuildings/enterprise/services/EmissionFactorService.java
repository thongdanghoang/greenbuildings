package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionFactorCriteriaDTO;
import greenbuildings.enterprise.entities.EmissionFactorEntity;
import greenbuildings.enterprise.entities.ExcelImportFileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface EmissionFactorService {
    
    Set<EmissionFactorEntity> findAllAvailable();
    
    List<EmissionFactorEntity> findBySource(UUID sourceId);

    Page<EmissionFactorEntity> search(SearchCriteriaDTO<EmissionFactorCriteriaDTO> searchCriteria, Pageable pageable);
    
    void delete(UUID id);
    
    Optional<EmissionFactorEntity> findById(UUID id);
    
    void createOrUpdate(EmissionFactorEntity factor);

    void importFactorFromExcel(MultipartFile file);

    void uploadExcelToMinio(MultipartFile file);

    ExcelImportFileEntity getExcelImportFile();
}
