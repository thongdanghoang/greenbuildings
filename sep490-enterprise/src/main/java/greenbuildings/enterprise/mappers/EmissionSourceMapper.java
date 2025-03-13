package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.EmissionSourceDTO;
import greenbuildings.enterprise.entities.EmissionSourceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmissionSourceMapper {

    EmissionSourceDTO toDTO(EmissionSourceEntity emissionSourceEntity);

}
