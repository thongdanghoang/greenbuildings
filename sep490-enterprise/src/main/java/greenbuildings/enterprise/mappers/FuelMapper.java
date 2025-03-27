package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.FuelDTO;
import greenbuildings.enterprise.entities.FuelEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FuelMapper {
    
    FuelDTO toDTO(FuelEntity fuelEntity);

    @Mapping(target = "id", ignore = true)
    FuelEntity createNewFuel(FuelDTO dto);

    @Mapping(target = "id", ignore = true)
    FuelEntity updateFuel(@MappingTarget FuelEntity entity, FuelDTO dto);


}
