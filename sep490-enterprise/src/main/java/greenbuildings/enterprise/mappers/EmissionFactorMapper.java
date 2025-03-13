package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.EmissionFactorDTO;
import greenbuildings.enterprise.entities.EmissionFactorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmissionFactorMapper {
    
    EmissionFactorDTO toDTO(EmissionFactorEntity emissionFactorEntity);
    
}
