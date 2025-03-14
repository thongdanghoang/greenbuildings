package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.EmissionActivityDTO;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmissionActivityMapper {
    
    @Mapping(target = "buildingID", ignore = true)
    @Mapping(target = "emissionFactorID", ignore = true)
    @Mapping(target = "emissionSourceID", ignore = true)
    EmissionActivityDTO toDTO(EmissionActivityEntity emissionActivityEntity);

}
