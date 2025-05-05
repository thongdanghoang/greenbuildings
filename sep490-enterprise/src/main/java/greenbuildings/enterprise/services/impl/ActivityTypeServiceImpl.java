package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.dtos.ActivityTypeCriteriaDTO;
import greenbuildings.enterprise.dtos.ActivityTypeDTO;
import greenbuildings.enterprise.entities.ActivityTypeEntity;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.mappers.ActivityTypeMapper;
import greenbuildings.enterprise.repositories.ActivityTypeRepository;
import greenbuildings.enterprise.repositories.BuildingRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.repositories.TenantRepository;
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
    private final TenantRepository tenantRepository;
    private final BuildingRepository buildingRepository;
    
    @Override
    public List<ActivityTypeEntity> findAll() {
        return repository.findAll();
    }
    
    @Override
    public List<ActivityTypeEntity> findByBuildingId(UUID buildingId) {
        if (Objects.isNull(buildingId) || !buildingRepository.existsById(buildingId)) {
            throw new NoSuchElementException("Building with ID " + buildingId + " not found");
        }
        return repository.findByBuildingId(buildingId);
    }
    
    @Override
    public ActivityTypeEntity create(ActivityTypeDTO dto) {
        if (Objects.isNull(dto.buildingId()) || !tenantRepository.existsById(dto.buildingId())) {
            throw new NoSuchElementException("Tenant with ID " + dto.buildingId() + " not found");
        }
        ActivityTypeEntity entity = mapper.toEntity(dto);
        return repository.save(entity);
    }
    
    @Override
    public Page<ActivityTypeEntity> search(ActivityTypeCriteriaDTO criteria, Pageable pageable) {
        var emissionSourceIDs = repository.findByName(criteria.buildingId(), criteria.name(), pageable);
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
        if (activityTypeEntity.getId() != null) {
            var oldActivityType = repository.findById(activityTypeEntity.getId()).orElseThrow();
            if (oldActivityType.getName().equalsIgnoreCase(activityTypeEntity.getName())) {
                repository.save(activityTypeEntity);
                return;
            }
        }
        if (repository.existsByNameActivityTypeInTenant(activityTypeEntity.getBuilding().getId(), activityTypeEntity.getName())) {
            throw new BusinessException("name", "business.activityType.name.exist");
        }
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