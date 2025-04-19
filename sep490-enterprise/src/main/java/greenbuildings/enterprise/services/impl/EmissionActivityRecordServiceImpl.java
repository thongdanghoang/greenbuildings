package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.enterprise.dtos.EmissionActivityRecordCriteria;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.entities.RecordFileEntity;
import greenbuildings.enterprise.repositories.EmissionActivityRecordRepository;
import greenbuildings.enterprise.repositories.RecordFileRepository;
import greenbuildings.enterprise.services.CalculationService;
import greenbuildings.enterprise.services.EmissionActivityRecordService;
import greenbuildings.enterprise.services.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class EmissionActivityRecordServiceImpl implements EmissionActivityRecordService {
    
    private final EmissionActivityRecordRepository recordRepository;
    private final MinioService minioService;
    private final RecordFileRepository fileRepository;
    private final CalculationService calculationService;
    
    @Override
    public Page<EmissionActivityRecordEntity> search(SearchCriteriaDTO<EmissionActivityRecordCriteria> searchCriteria) {
        Page<EmissionActivityRecordEntity> page = recordRepository
                .findAllByGroupItemId(
                        searchCriteria.criteria().emissionActivityId(),
                        CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort()));
        
        List<EmissionActivityRecordEntity> modifiedContent = calculationService.calculate(searchCriteria.criteria().emissionActivityId(), page.getContent());
        modifiedContent.stream()
                       .filter(x -> x.getGhg() != null)
                       .forEach(entity -> entity.setGhg(entity.getGhg().setScale(2, RoundingMode.HALF_UP)));
        return new PageImpl<>(modifiedContent, page.getPageable(), page.getTotalElements());
    }
    
    @Override
    public void createWithFile(EmissionActivityRecordEntity record, MultipartFile file) {
        boolean isSaving = record.getId() == null;
        
        if (isSaving) {
            newRecord(record, file);
        } else {
            updateRecord(record, file);
        }
    }
    
    private void newRecord(EmissionActivityRecordEntity entity, MultipartFile file) {
        if (recordRepository.existsByGroupItemIdAndDateOverlap(entity.getGroupItem().getId(), entity.getStartDate(), entity.getEndDate())) {
            throw new BusinessException("rangeDates", "business.record.dateOverlap");
        }
        entity = recordRepository.save(entity);
        if (file != null) {
            String minioPath = minioService.uploadFile(file, entity.getId().toString());
            
            RecordFileEntity fileEntity = new RecordFileEntity();
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setContentType(file.getContentType());
            fileEntity.setFileSize(file.getSize());
            fileEntity.setMinioPath(minioPath);
            fileEntity.setUploadDate(LocalDateTime.now());
            fileEntity.setRecord(entity);
            entity.setFile(fileEntity);
            
            recordRepository.save(entity);
        }
    }
    
    private void updateRecord(EmissionActivityRecordEntity entity, MultipartFile file) {
        if (recordRepository.otherExistsByGroupItemIdAndDateOverlap(entity.getId(), entity.getGroupItem().getId(), entity.getStartDate(), entity.getEndDate())) {
            throw new BusinessException("rangeDates", "business.record.dateOverlap");
        }
        EmissionActivityRecordEntity existing = recordRepository.findById(entity.getId()).orElseThrow();
        
        if (file == null) {
            entity.setFile(existing.getFile());
            recordRepository.save(entity);
        } else {
            try {
                if (existing.getFile() != null) {
                    minioService.deleteFile(existing.getFile().getMinioPath());
                    fileRepository.delete(existing.getFile());
                }
                
                String minioPath = minioService.uploadFile(file, entity.getId().toString());
                
                RecordFileEntity fileEntity = new RecordFileEntity();
                fileEntity.setFileName(file.getOriginalFilename());
                fileEntity.setContentType(file.getContentType());
                fileEntity.setFileSize(file.getSize());
                fileEntity.setMinioPath(minioPath);
                fileEntity.setUploadDate(LocalDateTime.now());
                fileEntity.setRecord(entity);
                entity.setFile(fileEntity);
                
                recordRepository.save(entity);
            } catch (Exception e) {
                throw new TechnicalException(e);
            }
        }
    }
    
    @Override
    public void deleteRecords(Set<UUID> ids) {
        List<EmissionActivityRecordEntity> list = recordRepository.findAllByIdIn(ids);
        if (list.isEmpty() || list.size() != ids.size()) {
            throw new BusinessException("ids", "http.error.status.404", Collections.emptyList());
        }
        for (EmissionActivityRecordEntity record : list) {
            if (record.getFile() != null) {
                minioService.deleteFile(record.getFile().getMinioPath());
            }
        }
        recordRepository.deleteAllById(ids);
    }
    
    @Override
    @Transactional
    public RecordFileEntity uploadFile(UUID recordId, MultipartFile file) {
        return null;
    }
    
    @Override
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
        file.getRecord().setFile(null);
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