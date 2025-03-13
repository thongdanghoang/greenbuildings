package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.FuelDTO;
import greenbuildings.enterprise.entities.FuelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FuelMapper {
    
    FuelDTO toDTO(FuelEntity fuelEntity);
    
}
