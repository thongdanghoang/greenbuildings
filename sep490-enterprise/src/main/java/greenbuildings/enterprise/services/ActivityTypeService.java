package greenbuildings.enterprise.services;

import greenbuildings.enterprise.dtos.ActivityTypeDTO;
import greenbuildings.enterprise.entities.ActivityTypeEntity;

import java.util.List;
import java.util.UUID;

public interface ActivityTypeService {
    List<ActivityTypeEntity> findAll();
    
    List<ActivityTypeEntity> findByBuildingId(UUID buildingId);
    
    ActivityTypeEntity create(ActivityTypeDTO dto);
}
