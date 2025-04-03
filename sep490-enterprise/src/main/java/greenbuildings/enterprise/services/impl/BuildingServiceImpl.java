package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.dtos.DownloadReportDTO;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.repositories.BuildingRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRecordRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.services.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {
    
    private final BuildingRepository buildingRepository;
    private final EmissionActivityRecordRepository recordRepo;
    private final EmissionActivityRepository activityRepo;
    
    @Override
    public BuildingEntity createBuilding(BuildingEntity building) {
        UUID enterpriseId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
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
    public Optional<BuildingEntity> findById(UUID id) {
        return buildingRepository.findByIdWithGraph(id);
    }
    
    @Override
    public Page<BuildingEntity> getEnterpriseBuildings(UUID enterpriseId, Pageable page) {
        return buildingRepository.findByEnterpriseIdAndDeleted(enterpriseId, false, page);
    }
    
    @Override
    public void deleteBuilding(UUID id) {
        UUID enterpriseId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
        var optionalBuilding = buildingRepository.findByIdAndEnterpriseId(id, enterpriseId);
        if (optionalBuilding.isPresent()) {
            var building = optionalBuilding.get();
            if (!building.isDeleted()) {
                building.setDeleted(true);
                buildingRepository.save(building);
            }
        }
    }
    
    @Override
    public void generateReport(DownloadReportDTO downloadReport) {
        BuildingEntity building = buildingRepository.findById(downloadReport.buildingID()).orElseThrow();
        List<EmissionActivityEntity> activities = activityRepo.findAllByIdIn(downloadReport.selectedActivities());
        
        if (activities.size() != downloadReport.selectedActivities().size()) {
            throw new BusinessException("activities", "validation.business.activities.notFound");
        }
        
        Map<EmissionActivityEntity, List<EmissionActivityRecordEntity>> records = new HashMap<>();
        
        for (EmissionActivityEntity activity : activities) {
            List<EmissionActivityRecordEntity> recordsByActivity = recordRepo
                    .findAllByEmissionActivityEntityIdAndDateBetween(activity.getId(), downloadReport.startDate(), downloadReport.endDate());
            records.put(activity, recordsByActivity);
        }
    }
    
    @Override
    public List<BuildingEntity> findBuildingsByEnterpriseId(UUID enterpriseId) {
        return buildingRepository.findAllByEnterpriseId(enterpriseId);
    }
    
}
