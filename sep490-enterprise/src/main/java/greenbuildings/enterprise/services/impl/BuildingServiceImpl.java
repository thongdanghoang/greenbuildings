package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.enterprise.dtos.DownloadReportDTO;
import greenbuildings.enterprise.dtos.OverviewBuildingDTO;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.entities.EnterpriseEntity;
import greenbuildings.enterprise.enums.EmissionUnit;
import greenbuildings.enterprise.repositories.BuildingGroupRepository;
import greenbuildings.enterprise.repositories.BuildingRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRecordRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.services.BuildingService;
import greenbuildings.enterprise.services.CalculationService;
import greenbuildings.enterprise.utils.CalculationUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {
    
    public static final int START_RECORD_ROW_IDX = 14;
    private final BuildingRepository buildingRepository;
    private final EmissionActivityRecordRepository recordRepo;
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
    public Optional<BuildingEntity> findById(UUID id) {
        return buildingRepository.findByIdWithGraph(id);
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
    public List<BuildingEntity> findBuildingsByEnterpriseId(UUID enterpriseId) {
        return buildingRepository.findAllByEnterpriseId(enterpriseId);
    }
    
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
