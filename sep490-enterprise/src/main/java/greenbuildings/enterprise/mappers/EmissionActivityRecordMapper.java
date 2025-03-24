package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.EmissionActivityRecordDTO;
import greenbuildings.enterprise.dtos.NewEmissionActivityRecordDTO;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.mappers.decorators.EmissionActivityRecordMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@DecoratedWith(EmissionActivityRecordMapperDecorator.class)
public interface EmissionActivityRecordMapper {

    EmissionActivityRecordDTO toDTO(EmissionActivityRecordEntity emissionActivityEntity);

    @Mapping(target = "emissionActivityEntity", ignore = true)
    EmissionActivityRecordEntity newToEntity(NewEmissionActivityRecordDTO emissionActivityRecordDTO);
}
