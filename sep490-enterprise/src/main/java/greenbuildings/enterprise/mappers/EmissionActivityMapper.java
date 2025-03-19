package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.EmissionActivityDTO;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.mappers.decorators.EmissionActivityMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@DecoratedWith(EmissionActivityMapperDecorator.class)
public interface EmissionActivityMapper {
    
    @Mapping(target = "buildingID", ignore = true)
    @Mapping(target = "emissionFactorID", ignore = true)
    @Mapping(target = "records", ignore = true)
    EmissionActivityDTO toDTO(EmissionActivityEntity emissionActivityEntity);
    
    @Mapping(target = "building", ignore = true)
    @Mapping(target = "emissionFactorEntity", ignore = true)
    EmissionActivityEntity createNewActivity(EmissionActivityDTO dto);

}
