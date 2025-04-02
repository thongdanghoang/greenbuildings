package greenbuildings.enterprise.services;

import greenbuildings.enterprise.entities.BuildingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BuildingService {
    
    BuildingEntity createBuilding(BuildingEntity building);
    
    Optional<BuildingEntity> findById(UUID id);
    
    Page<BuildingEntity> getEnterpriseBuildings(UUID enterpriseId, Pageable page);

    void deleteBuilding(UUID id);
    
    List<BuildingEntity> findBuildingsByEnterpriseId(UUID enterpriseId);
}
