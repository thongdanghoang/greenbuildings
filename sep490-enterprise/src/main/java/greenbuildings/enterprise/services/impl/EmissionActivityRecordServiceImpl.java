package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.dtos.EmissionActivityRecordCriteria;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.entities.RecordFileEntity;
import greenbuildings.enterprise.repositories.EmissionActivityRecordRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.repositories.RecordFileRepository;
import greenbuildings.enterprise.services.EmissionActivityRecordService;
import greenbuildings.enterprise.services.MinioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
@Transactional(rollbackOn = Throwable.class)
@RequiredArgsConstructor
public class EmissionActivityRecordServiceImpl implements EmissionActivityRecordService {

    private final EmissionActivityRecordRepository recordRepository;
    private final EmissionActivityRepository activityRepository;
    private final MinioService minioService;
    private final RecordFileRepository fileRepository;

    @Override
    public Page<EmissionActivityRecordEntity> search(SearchCriteriaDTO<EmissionActivityRecordCriteria> searchCriteria) {
        return recordRepository
                .findAllByEmissionActivityEntityId(
                        searchCriteria.criteria().emissionActivityId(),
                        CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort()));
    }
    
    @Override
    public EmissionActivityRecordEntity addOrUpdate(EmissionActivityRecordEntity entity) {
        return recordRepository.save(entity);
    }

        
    @Override
    public void createWithFile(EmissionActivityRecordEntity record, MultipartFile file) {
        record = recordRepository.save(record);
        
        if (file != null) {
            String minioPath = minioService.uploadFile(file, record.getId().toString());
            
            RecordFileEntity fileEntity = new RecordFileEntity();
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setContentType(file.getContentType());
            fileEntity.setFileSize(file.getSize());
            fileEntity.setMinioPath(minioPath);
            fileEntity.setUploadDate(LocalDateTime.now());
            fileEntity.setRecord(record);
            record.setFile(fileEntity);
            
            recordRepository.save(record);
        }
    }
    
    @Override
    public void deleteRecords(Set<UUID> ids) {
        if (!recordRepository.existsAllById(ids)) {
            throw new BusinessException("ids", "http.error.status.404", Collections.emptyList());
        }
        recordRepository.deleteAllById(ids);
    }

    @Override
    @Transactional
    public RecordFileEntity uploadFile(UUID recordId, MultipartFile file) {
        return null;
    }
    
    @Override
    @Transactional
    public void deleteFile(UUID recordId, UUID fileId) {
        EmissionActivityRecordEntity record = recordRepository.findById(recordId)
            .orElseThrow(() -> new BusinessException(null, "Record not found"));
            
        RecordFileEntity file = fileRepository.findById(fileId)
            .orElseThrow(() -> new BusinessException(null, "File not found"));
            
        if (!file.getRecord().getId().equals(recordId)) {
            throw new BusinessException(null, "File does not belong to this record");
        }
        
        // Delete from MinIO
        minioService.deleteFile(file.getMinioPath());
        
        // Delete from database
        fileRepository.delete(file);
    }
    
    @Override
    public String getFileUrl(UUID recordId, UUID fileId) {
        RecordFileEntity file = fileRepository.findById(fileId)
            .orElseThrow(() -> new BusinessException(null, "File not found"));
            
        if (!file.getRecord().getId().equals(recordId)) {
            throw new BusinessException(null, "File does not belong to this record");
        }
        
        return minioService.getFileUrl(file.getMinioPath());
    }
    
    @Override
    public List<RecordFileEntity> getRecordFiles(UUID recordId) {
        return fileRepository.findByRecordId(recordId);
    }
} 