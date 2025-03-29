package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.EmissionSourceDTO;
import greenbuildings.enterprise.entities.EmissionSourceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmissionSourceMapper {

    EmissionSourceDTO toDTO(EmissionSourceEntity emissionSourceEntity);

    @Mapping(target = "id", ignore = true)
    EmissionSourceEntity createNewEmissionSource(EmissionSourceDTO dto);

    @Mapping(target = "id", ignore = true)
    EmissionSourceEntity updateEmissionSource(@MappingTarget EmissionSourceEntity entity, EmissionSourceDTO dto);

}
