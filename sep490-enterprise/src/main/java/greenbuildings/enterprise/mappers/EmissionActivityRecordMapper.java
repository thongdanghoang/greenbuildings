package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.EmissionActivityRecordDTO;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmissionActivityRecordMapper {

    EmissionActivityRecordDTO toDTO(EmissionActivityRecordEntity emissionActivityEntity);

}
