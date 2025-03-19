package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.EmissionFactorDTO;
import greenbuildings.enterprise.entities.EmissionFactorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmissionFactorMapper {
    
    @Mapping(source = "source", target = "emissionSourceDTO")
    @Mapping(source = "energyConversion", target = "energyConversionDTO")
    EmissionFactorDTO toDTO(EmissionFactorEntity emissionFactorEntity);
    
}
