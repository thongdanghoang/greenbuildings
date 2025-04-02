package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.EnergyConversionDTO;
import greenbuildings.enterprise.entities.EnergyConversionEntity;
import greenbuildings.enterprise.mappers.decorators.EnergyConversionMapperDecorator;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@DecoratedWith(EnergyConversionMapperDecorator.class)
public interface EnergyConversionMapper {
    
    EnergyConversionDTO toDTO(EnergyConversionEntity energyConversionEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fuel", ignore = true)
    EnergyConversionEntity createNewEnergyConversion(EnergyConversionDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fuel", ignore = true)
    EnergyConversionEntity updateEnergyConversion(@MappingTarget EnergyConversionEntity entity, EnergyConversionDTO dto);

}
