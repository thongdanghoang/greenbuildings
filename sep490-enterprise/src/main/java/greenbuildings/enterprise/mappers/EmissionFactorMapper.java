package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.EmissionFactorDTO;
import greenbuildings.enterprise.entities.EmissionFactorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmissionFactorMapper {
    
    @Mapping(source = "source", target = "emissionSourceDTO")
    @Mapping(source = "energyConversion", target = "energyConversionDTO")
    EmissionFactorDTO toDTO(EmissionFactorEntity emissionFactorEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "emissionSourceDTO", target = "source")
    @Mapping(source = "energyConversionDTO", target = "energyConversion")
    EmissionFactorEntity toEntity(EmissionFactorDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "emissionSourceDTO", target = "source")
    @Mapping(source = "energyConversionDTO", target = "energyConversion")
    @Mapping(target = "active", ignore = true)
    EmissionFactorEntity toEntityUpdate(@MappingTarget EmissionFactorEntity entity, EmissionFactorDTO dto);
}
