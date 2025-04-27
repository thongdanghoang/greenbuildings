package greenbuildings.enterprise.mappers.decorators;

import greenbuildings.enterprise.dtos.CreateEmissionActivityDTO;
import greenbuildings.enterprise.entities.ActivityTypeEntity;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.mappers.EmissionActivityMapper;
import greenbuildings.enterprise.repositories.ActivityTypeRepository;
import greenbuildings.enterprise.repositories.BuildingGroupRepository;
import greenbuildings.enterprise.repositories.BuildingRepository;
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
    private BuildingRepository buildingRepo;
    
    @Autowired
    private ActivityTypeRepository activityTypeRepo;
    
    @Autowired
    private BuildingGroupRepository buildingGroupRepository;
    
    
    @Override
    public EmissionActivityEntity createNewActivity(CreateEmissionActivityDTO dto) {
        EmissionActivityEntity entity = delegate.createNewActivity(dto);
        entity.setBuildingGroup(buildingGroupRepository.findById(dto.buildingGroupID()).orElseThrow());
        entity.setEmissionFactorEntity(factorRepo.findById(dto.emissionFactorID()).orElseThrow());
        mapEmissionType(dto, entity);
        return entity;
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
            ActivityTypeEntity type = new ActivityTypeEntity();
            type.setBuilding(entity.getBuilding());
            type.setName(dto.type());
            entity.setType(type);
        }
    }
    
    @Override
    public EmissionActivityEntity updateActivity(CreateEmissionActivityDTO dto) {
        EmissionActivityEntity entity = delegate.createNewActivity(dto);
        mapEmissionType(dto, entity);
        return entity;
    }
}
