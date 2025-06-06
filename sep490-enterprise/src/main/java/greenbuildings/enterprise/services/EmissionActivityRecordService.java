package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionActivityRecordCriteria;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.entities.RecordFileEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface EmissionActivityRecordService {
    Page<EmissionActivityRecordEntity> search(SearchCriteriaDTO<EmissionActivityRecordCriteria> searchCriteria);

    void deleteRecords(Set<UUID> ids);
    
    // File operations
    RecordFileEntity uploadFile(UUID recordId, MultipartFile file);
    
    void deleteFile(UUID recordId, UUID fileId);
    
    RecordFileEntity getFile(UUID recordId, UUID fileId);
    
    List<RecordFileEntity> getRecordFiles(UUID recordId);
    
    void createWithFile(EmissionActivityRecordEntity entity, MultipartFile file);
}