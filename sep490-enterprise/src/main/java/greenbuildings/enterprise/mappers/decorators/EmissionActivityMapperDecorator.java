package greenbuildings.enterprise.mappers.decorators;

import greenbuildings.enterprise.dtos.EmissionActivityDTO;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.mappers.EmissionActivityMapper;
import greenbuildings.enterprise.repositories.BuildingRepository;
import greenbuildings.enterprise.repositories.EmissionFactorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public abstract class EmissionActivityMapperDecorator implements EmissionActivityMapper {
    
    @Autowired
    @Qualifier("delegate")
    private EmissionActivityMapper delegate;
    
    @Autowired
    private EmissionFactorRepository factorRepo;
    
    @Autowired
    private BuildingRepository buildingRepo;
    
    
    @Override
    public EmissionActivityEntity createNewActivity(EmissionActivityDTO dto) {
        EmissionActivityEntity entity = delegate.createNewActivity(dto);
        entity.setBuilding(buildingRepo.findById(dto.buildingID()).orElseThrow());
        entity.setEmissionFactorEntity(factorRepo.findById(dto.emissionFactorID()).orElseThrow());
        return entity;
    }
}
