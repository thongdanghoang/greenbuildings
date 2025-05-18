package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.dtos.OverviewBuildingDTO;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.interceptors.BuildingPermissionFilter;
import greenbuildings.enterprise.repositories.BuildingRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.services.BuildingService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {
    
    public static final int START_RECORD_ROW_IDX = 14;
    private final BuildingRepository buildingRepository;
    private final EmissionActivityRepository activityRepo;
    
    @Override
    public BuildingEntity createBuilding(BuildingEntity building, UUID enterpriseId) {
        if (building.getId() != null) {
            var oldBuilding = buildingRepository.findById(building.getId()).orElseThrow();
            if (oldBuilding.getName().equals(building.getName())) {
                return buildingRepository.save(building);
            }
        }
        if (buildingRepository.existsByNameBuildingInEnterprise(building.getName(), enterpriseId)) {
            throw new BusinessException("name", "business.buildings.name.exist");
        }
        return buildingRepository.save(building);
    }
    
    @Override
    public BuildingEntity findById(UUID id) {
        return buildingRepository.getWithGraph(id, BuildingEntity.class, new String[]{BuildingEntity.Fields.buildingGroups});
    }
    
    @Override
    public Page<BuildingEntity> getEnterpriseBuildings(UUID enterpriseId, Pageable page) {
        return buildingRepository.findByEnterpriseId(enterpriseId, page);
    }
    
    @Override
    public void deleteBuilding(UUID id, UUID enterpriseId) {
        var optionalBuilding = buildingRepository.findByIdAndEnterpriseId(id, enterpriseId);
        if (optionalBuilding.isPresent()) {
            buildingRepository.deleteById(id);
        }
    }
    
    
    @Override
    @BuildingPermissionFilter
    public List<BuildingEntity> findBuildingsByEnterpriseId(UUID enterpriseId) {
        return buildingRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    @Override
    public OverviewBuildingDTO getOverviewBuildingById(UUID id) {
        var building = buildingRepository.findById(id).orElseThrow();
        long numberOfGroups = building.getBuildingGroups().size();
        long numberOfCorporationTenant = building.getBuildingGroups()
                                                 .stream()
                                                 .filter(group -> group.getTenant() != null)
                                                 .count();
        long numberOfActivities = building.getBuildingGroups()
                                          .stream()
                                          .mapToLong(group -> group.getEmissionActivities().size())
                                          .sum();
        long numberOfCommonActivities = activityRepo.countByBuildingIdAndBuildingGroupIsNull(id);
        
        return new OverviewBuildingDTO(numberOfGroups, numberOfCorporationTenant, numberOfActivities, numberOfCommonActivities);
    }
    
}
