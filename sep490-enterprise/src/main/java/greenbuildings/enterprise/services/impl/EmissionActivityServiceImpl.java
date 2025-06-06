package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.enterprise.dtos.EmissionActivityCriteria;
import greenbuildings.enterprise.dtos.emission_activities.ActivityCriteria;
import greenbuildings.enterprise.entities.ActivityTypeEntity;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.entities.EmissionFactorEntity;
import greenbuildings.enterprise.entities.EmissionSourceEntity;
import greenbuildings.enterprise.entities.SubscriptionEntity;
import greenbuildings.enterprise.interceptors.ActivityAwareBuildingPermissionFilter;
import greenbuildings.enterprise.interceptors.BuildingPermissionFilter;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    
    @Transactional(readOnly = true)
    @Override
    @ActivityAwareBuildingPermissionFilter
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
    
    @Transactional(readOnly = true)
    @Override
    @ActivityAwareBuildingPermissionFilter
    public Page<EmissionActivityEntity> search(Pageable pageable, UUID enterpriseId, ActivityCriteria criteria) {
        var activityIDs = emissionActivityRepository
                .findBy(EmissionActivitySpecifications.withFilters(enterpriseId, criteria),
                        q -> q.project().as(IdProjection.class).page(pageable))
                .map(IdProjection::getId);
        
        var searchResults = emissionActivityRepository
                .findWithRecords(activityIDs.toSet(), pageable.getSort())
                .stream().map(calculationService::calculate)
                .toList();
        
        return new PageImpl<>(searchResults, pageable, activityIDs.getTotalElements());
    }
    
    @Override
    @Transactional(readOnly = true)
    @BuildingPermissionFilter
    public List<BuildingEntity> getBuildingsByEnterpriseId() {
        return buildingRepository.findAll();
    }
    
    @Transactional(readOnly = true)
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
            if (allValidSubscriptions.isEmpty()
                || !allValidSubscriptions.getFirst().isValid()
                || noActivities >= allValidSubscriptions.getFirst().getMaxNumberOfDevices()) {
                throw new BusinessException("maxNumberOfActivities", "validation.subscription.noActivities");
            }
            return emissionActivityRepository.save(entity);
        }
        var existing = emissionActivityRepository.findById(entity.getId()).orElseThrow();
        updateActivity(entity, existing);
        emissionActivityRepository.saveAndFlush(existing);
        
        return existing;
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
    
    @Transactional(readOnly = true)
    @Override
    @ActivityAwareBuildingPermissionFilter
    public EmissionActivityEntity getEmissionActivityDetails(UUID id) {
        return emissionActivityRepository.findDetailsById(id).orElseThrow(() -> new BusinessException("id", "http.error.status.404", Collections.emptyList()));
    }
    
    @Transactional(readOnly = true)
    @Override
    @ActivityAwareBuildingPermissionFilter
    public List<EmissionActivityEntity> getAllActivitiesByBuildingGroupId(UUID id) {
        return emissionActivityRepository.findByBuildingGroupId(id);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<ActivityRecordDateRange> findRecordedDateRangesById(UUID activityId, UUID excludeRecordId, UUID assetId) {
        return emissionActivityRepository.findRecordedDateRangesById(activityId, excludeRecordId, assetId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<BuildingEntity, BigDecimal> getTopBuildingsWithHighestEmissions(List<EmissionActivityEntity> activities, int limit) {
        // Group by BuildingEntity and sum totalEmission
        var buildingToTotalGhg = activities
                .stream()
                .collect(Collectors.groupingBy(
                        EmissionActivityEntity::getBuilding,
                        Collectors.mapping(
                                EmissionActivityEntity::getTotalEmission,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                                          )
                                              ));
        // Sort and limit to top N
        return buildingToTotalGhg
                .entrySet().stream()
                .sorted(Map.Entry.<BuildingEntity, BigDecimal>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        java.util.LinkedHashMap::new
                                         ));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<EmissionSourceEntity, BigDecimal> getTopEmissionSourcesWithHighestEmissions(List<EmissionActivityEntity> activities, int limit) {
        // Group by EmissionSourceEntity and sum totalEmission
        var sourceToTotalGhg = activities
                .stream()
                .collect(Collectors.groupingBy(
                        a -> a.getEmissionFactorEntity().getSource(),
                        Collectors.mapping(
                                EmissionActivityEntity::getTotalEmission,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                                          )
                                              ));
        // Sort and limit to top N
        return sourceToTotalGhg
                .entrySet().stream()
                .sorted(Map.Entry.<EmissionSourceEntity, BigDecimal>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        java.util.LinkedHashMap::new
                                         ));
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalEmissions(List<EmissionActivityEntity> activities) {
        return activities
                .stream()
                .map(EmissionActivityEntity::getTotalEmission)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmissionActivityEntity> calculationActivitiesTotalGhg(List<EmissionActivityEntity> activities) {
        var factorIDs = activities.stream()
                                  .map(EmissionActivityEntity::getEmissionFactorEntity)
                                  .map(EmissionFactorEntity::getId)
                                  .collect(Collectors.toUnmodifiableSet());
        var factors = emissionFactorRepository
                .findAllByIdIn(factorIDs)
                .stream()
                .collect(Collectors.toMap(EmissionFactorEntity::getId, Function.identity()));
        activities.forEach(activity -> {
            if (factors.containsKey(activity.getEmissionFactorEntity().getId())) {
                activity.setEmissionFactorEntity(factors.get(activity.getEmissionFactorEntity().getId()));
            }
        });
        activities = activities.stream().map(calculationService::calculate).toList();
        // Calculate total GHG for each activity
        activities.forEach(activity ->
                                   activity.setTotalEmission(
                                           activity.getRecords().stream()
                                                   .map(EmissionActivityRecordEntity::getGhg)
                                                   .filter(Objects::nonNull)
                                                   .reduce(BigDecimal.ZERO, BigDecimal::add)
                                                            )
                          );
        return activities;
    }
    
    @Override
    @ActivityAwareBuildingPermissionFilter
    public List<EmissionActivityEntity> findByEnterpriseIdFetchRecords(UUID enterpriseId) {
        return emissionActivityRepository.findByEnterpriseId(enterpriseId);
    }
    
    @Override
    @ActivityAwareBuildingPermissionFilter
    public List<EmissionActivityEntity> findByEnterpriseAndBuildingsFetchRecords(UUID enterpriseId, Set<UUID> buildings) {
        return emissionActivityRepository.findByEnterpriseAndBuildings(enterpriseId, buildings);
    }
    
}
