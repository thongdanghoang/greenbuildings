package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.dtos.ActivityTypeCriteriaDTO;
import greenbuildings.enterprise.dtos.ActivityTypeDTO;
import greenbuildings.enterprise.entities.ActivityTypeEntity;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.mappers.ActivityTypeMapper;
import greenbuildings.enterprise.repositories.ActivityTypeRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.repositories.EnterpriseRepository;
import greenbuildings.enterprise.services.ActivityTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class ActivityTypeServiceImpl implements ActivityTypeService {
    
    private final ActivityTypeRepository repository;
    private final EmissionActivityRepository emissionActivityRepository;
    private final ActivityTypeMapper mapper;
    private final EnterpriseRepository enterpriseRepository;
    
    @Override
    public List<ActivityTypeEntity> findAll() {
        return repository.findAll();
    }
    
    @Override
    public List<ActivityTypeEntity> findByEnterpriseId(UUID enterpriseId) {
        if (!enterpriseRepository.existsById(enterpriseId)) {
            throw new NoSuchElementException("Enterprise with ID " + enterpriseId + " not found");
        }
        return repository.findByEnterpriseId(enterpriseId);
    }
    
    @Override
    public ActivityTypeEntity create(ActivityTypeDTO dto) {
        if (Objects.isNull(dto.enterpriseId()) || !enterpriseRepository.existsById(dto.enterpriseId())) {
            throw new NoSuchElementException("Enterprise with ID " + dto.enterpriseId() + " not found");
        }
        ActivityTypeEntity entity = mapper.toEntity(dto);
        return repository.save(entity);
    }
    
    @Override
    public Page<ActivityTypeEntity> search(SearchCriteriaDTO<ActivityTypeCriteriaDTO> searchCriteria, Pageable pageable) {
        UUID enterpriseId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
        var emissionSourceIDs = repository.findByName(
                searchCriteria.criteria().criteria(),
                pageable, enterpriseId);
        var results = repository.findAllById(emissionSourceIDs.toSet())
                                .stream()
                                .collect(Collectors.toMap(ActivityTypeEntity::getId, Function.identity()));
        return emissionSourceIDs.map(results::get);
    }
    
    @Override
    public Optional<ActivityTypeEntity> findById(UUID id) {
        return repository.findById(id);
    }
    
    @Override
    public void createOrUpdate(ActivityTypeEntity activityTypeEntity) {
        repository.save(activityTypeEntity);
    }
    
    @Override
    public void delete(Set<UUID> typeIds) {
        // Validate input
        if (CollectionUtils.isEmpty(typeIds)) {
            throw new BusinessException("activityType", "activityType.delete.no.ids", Collections.emptyList());
        }
        
        // Fetch all types in one query
        List<ActivityTypeEntity> types = repository.findAllById(typeIds);
        if (types.isEmpty()) {
            return; // Nothing to delete
        }
        
        // Extract type IDs for batch processing
        Set<UUID> validTypeIds = types.stream()
                                      .map(ActivityTypeEntity::getId)
                                      .collect(Collectors.toSet());
        
        // Update related emission activities in batch
        List<EmissionActivityEntity> emissionActivities = emissionActivityRepository.findAllByTypeIdIn(validTypeIds);
        emissionActivities.forEach(activity -> activity.setType(null));
        
        // Save updates to emission activities
        emissionActivityRepository.saveAll(emissionActivities);
        
        // Delete all types in one operation
        repository.deleteAll(types);
    }
} 