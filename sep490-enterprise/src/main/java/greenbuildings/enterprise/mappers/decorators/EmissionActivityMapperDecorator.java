package greenbuildings.enterprise.mappers.decorators;

import greenbuildings.enterprise.dtos.CreateEmissionActivityDTO;
import greenbuildings.enterprise.entities.ActivityTypeEntity;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.mappers.EmissionActivityMapper;
import greenbuildings.enterprise.repositories.ActivityTypeRepository;
import greenbuildings.enterprise.repositories.EmissionFactorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public abstract class EmissionActivityMapperDecorator implements EmissionActivityMapper {
    
    @Autowired
    @Qualifier("delegate")
    private EmissionActivityMapper delegate;
    
    @Autowired
    private EmissionFactorRepository factorRepo;
    
    @Autowired
    private ActivityTypeRepository activityTypeRepo;
    
    
    @Override
    public EmissionActivityEntity createNewActivity(CreateEmissionActivityDTO dto) {
        var entity = delegate.createNewActivity(dto);
        mapBuildingAndBuildingGroup(dto, entity);
        entity.setEmissionFactorEntity(factorRepo.findById(dto.emissionFactorID()).orElseThrow());
        mapEmissionType(dto, entity);
        return entity;
    }
    
    @Override
    public EmissionActivityEntity updateActivity(CreateEmissionActivityDTO dto) {
        EmissionActivityEntity entity = delegate.createNewActivity(dto);
        mapBuildingAndBuildingGroup(dto, entity);
        mapEmissionType(dto, entity);
        return entity;
    }
    
    private static void mapBuildingAndBuildingGroup(CreateEmissionActivityDTO dto, EmissionActivityEntity entity) {
        if(dto.buildingId() != null) {
            var building = new BuildingEntity();
            building.setId(dto.buildingId());
            entity.setBuilding(building);
        } else {
            entity.setBuilding(null);
        }
        if (dto.buildingGroupID() != null) {
            var buildingGroup = new BuildingGroupEntity();
            buildingGroup.setId(dto.buildingGroupID());
            entity.setBuildingGroup(buildingGroup);
        } else {
            entity.setBuildingGroup(null);
        }
    }
    
    private void mapEmissionType(CreateEmissionActivityDTO dto, EmissionActivityEntity entity) {
        if (dto.type() == null) {
            return;
        }
        try {
            UUID typeId = UUID.fromString(dto.type());
            ActivityTypeEntity activityTypeEntity = activityTypeRepo.findById(typeId).orElseThrow();
            activityTypeEntity.setBuilding(entity.getBuilding());
            entity.setType(activityTypeEntity);
        } catch (IllegalArgumentException ex) {
            ActivityTypeEntity dbType = activityTypeRepo.findByNameIgnoreCaseAndBuildingId(dto.type(), entity.getBuilding().getId());
            if (dbType != null) {
                entity.setType(dbType);
            } else {
                ActivityTypeEntity type = new ActivityTypeEntity();
                type.setBuilding(entity.getBuilding());
                type.setName(dto.type());
                entity.setType(type);
            }
        }
    }
}
