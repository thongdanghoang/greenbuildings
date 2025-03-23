package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.repositories.BuildingRepository;
import greenbuildings.enterprise.services.BuildingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackOn = Throwable.class)
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {
    
    private final BuildingRepository buildingRepository;
    
    @Override
    public BuildingEntity createBuilding(BuildingEntity building) {
      UUID enterpriseId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
      if(buildingRepository.existsByNameBuildingInEnterprise(building.getName(),enterpriseId)) {
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
        return buildingRepository.findByEnterpriseIdAndDeleted(enterpriseId,false, page);
    }
     @Override
    public void deleteBuilding(UUID id) {
        UUID enterpriseId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
         var optionalBuilding = buildingRepository.findByIdAndEnterpriseId(id, enterpriseId);
         if(optionalBuilding.isPresent()) {
             var building = optionalBuilding.get();
             if(!building.isDeleted()) {
                 building.setDeleted(true);
                 buildingRepository.save(building);
             }
         }
    }

}
