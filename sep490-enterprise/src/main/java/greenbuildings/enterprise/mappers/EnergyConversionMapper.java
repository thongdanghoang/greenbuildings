package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.EnergyConversionDTO;
import greenbuildings.enterprise.entities.EnergyConversionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EnergyConversionMapper {
    
    EnergyConversionDTO toDTO(EnergyConversionEntity energyConversionEntity);

}
