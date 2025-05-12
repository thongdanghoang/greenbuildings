package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.dtos.EmissionActivityCriteria;
import greenbuildings.enterprise.dtos.emission_activities.ActivityCriteria;
import greenbuildings.enterprise.entities.ActivityTypeEntity;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.entities.EmissionFactorEntity;
import greenbuildings.enterprise.entities.SubscriptionEntity;
import greenbuildings.enterprise.models.ActivityRecordDateRange;
import greenbuildings.enterprise.models.IdProjection;
import greenbuildings.enterprise.repositories.ActivityTypeRepository;
import greenbuildings.enterprise.repositories.BuildingGroupRepository;
import greenbuildings.enterprise.repositories.BuildingRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.repositories.EmissionFactorRepository;
import greenbuildings.enterprise.repositories.SubscriptionRepository;
import greenbuildings.enterprise.repositories.specifications.EmissionActivitySpecifications;
import greenbuildings.enterprise.services.CalculationService;
import greenbuildings.enterprise.services.EmissionActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Throwable.class)
@Slf4j
@RequiredArgsConstructor
public class EmissionActivityServiceImpl implements EmissionActivityService {
    
    private final EmissionActivityRepository emissionActivityRepository;
    private final EmissionFactorRepository emissionFactorRepository;
    private final BuildingRepository buildingRepository;
    private final BuildingGroupRepository buildingGroupRepository;
    private final ActivityTypeRepository typeRepository;
    private final CalculationService calculationService;
    private final SubscriptionRepository subscriptionRepository;
    
    @Override
    public Page<EmissionActivityEntity> search(SearchCriteriaDTO<EmissionActivityCriteria> searchCriteria) {
        if (searchCriteria.criteria().buildingId() != null) {
            return emissionActivityRepository
                    .findAllByBuildingIdAndNameContainingIgnoreCaseAndBuildingGroupIsNull(
                            searchCriteria.criteria().buildingId(),
                            Optional.ofNullable(searchCriteria.criteria().name()).orElse(""),
                            CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort()));
        }
        return emissionActivityRepository
                .findAllByBuildingGroupIdAndNameContainingIgnoreCase(
                        searchCriteria.criteria().buildingGroupId(),
                        Optional.ofNullable(searchCriteria.criteria().name()).orElse(""),
                        CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort()));
    }
    
    @Override
    public Page<EmissionActivityEntity> search(Pageable pageable, UUID enterpriseId, ActivityCriteria criteria) {
        var activityIDs = emissionActivityRepository
                .findBy(EmissionActivitySpecifications.withFilters(enterpriseId, criteria),
                        q -> q.project().as(IdProjection.class).page(pageable)) // NOTE: paging only, sorting not applied in query in case use JpaSpecifications
                .map(IdProjection::getId);
        
        var searchResults = emissionActivityRepository
                .findWithRecords(activityIDs.toSet(), pageable.getSort()) // sorting applied here => return this collection to remains sorted
                .stream().map(calculationService::calculate)
                .toList();
        
        return new PageImpl<>(searchResults, pageable, activityIDs.getTotalElements());
    }
    
    @Override
    public List<BuildingEntity> getBuildingsByEnterpriseId(UUID enterpriseId) {
        return buildingRepository.getBuildingsByEnterpriseId(enterpriseId, LocalDate.now());
    }
    
    @Override
    public List<EmissionFactorEntity> getEmissionFactorsByEnterpriseId(UUID enterpriseId) {
        return emissionFactorRepository.getEmissionFactorsByEnterpriseId(enterpriseId);
    }
    
    @Override
    public EmissionActivityEntity addOrUpdate(EmissionActivityEntity entity) {
        mapActivityType(entity);
        if (entity.getId() == null) {
            List<SubscriptionEntity> allValidSubscriptions = subscriptionRepository.findAllValidSubscriptions(LocalDate.now(),
                                                                                                              entity.getBuilding().getId());
            long noActivities = emissionActivityRepository.countByBuildingId(entity.getBuilding().getId());
            if (allValidSubscriptions.isEmpty() || !allValidSubscriptions.get(0).isValid() || noActivities >= allValidSubscriptions.get(0).getMaxNumberOfDevices()) {
                throw new BusinessException("maxNumberOfActivities", "validation.subscription.noActivities");
            }
            entity = emissionActivityRepository.save(entity);
            return emissionActivityRepository.findDetailsById(entity.getId()).orElseThrow();
        }
        EmissionActivityEntity existing = emissionActivityRepository.findById(entity.getId()).orElseThrow();
        updateActivity(entity, existing);
        emissionActivityRepository.save(existing);
        
        return emissionActivityRepository.findDetailsById(entity.getId()).orElseThrow();
    }
    
    private void mapActivityType(EmissionActivityEntity entity) {
        if (entity.getType() == null) {
            return;
        }
        if (entity.getType().getId() == null) {
            ActivityTypeEntity type = typeRepository.save(entity.getType());
            entity.setType(type);
        } else {
            entity.setType(typeRepository.findById(entity.getType().getId()).orElseThrow());
        }
    }
    
    private void updateActivity(EmissionActivityEntity entity, EmissionActivityEntity existing) {
        // TODO: why manually update these fields, especially when using @Transactional?
        existing.setName(entity.getName());
        existing.setType(entity.getType());
        existing.setCategory(entity.getCategory());
        existing.setDescription(entity.getDescription());
        existing.setBuilding(entity.getBuilding());
        if (Objects.nonNull(entity.getBuildingGroup())
            && buildingGroupRepository.existsByIdAndBuildingId(entity.getBuildingGroup().getId(), entity.getBuilding().getId())) {
            existing.setBuildingGroup(entity.getBuildingGroup());
        } else {
            existing.setBuildingGroup(null);
        }
    }
    
    @Override
    public void deleteActivities(Set<UUID> ids) {
        if (!emissionActivityRepository.existsAllById(ids)) {
            throw new BusinessException("ids", "http.error.status.404", Collections.emptyList());
        }
        emissionActivityRepository.deleteAllById(ids);
    }
    
    @Override
    public EmissionActivityEntity getEmissionActivityDetails(UUID id) {
        return emissionActivityRepository.findDetailsById(id).orElseThrow(() -> new BusinessException("id", "http.error.status.404", Collections.emptyList()));
    }
    
    @Override
    public List<EmissionActivityEntity> getAllActivitiesByBuildingId(UUID id) {
        return emissionActivityRepository.findByBuildingGroupId(id);
    }
    
    @Override
    public List<ActivityRecordDateRange> findRecordedDateRangesById(UUID activityId, UUID excludeRecordId) {
        return emissionActivityRepository.findRecordedDateRangesById(activityId, excludeRecordId);
    }
}
