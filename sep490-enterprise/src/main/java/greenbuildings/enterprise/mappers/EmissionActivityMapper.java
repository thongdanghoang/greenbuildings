package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.EmissionActivityDTO;
import greenbuildings.enterprise.dtos.EmissionActivityDetailsDTO;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.mappers.decorators.EmissionActivityMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@DecoratedWith(EmissionActivityMapperDecorator.class)
public interface EmissionActivityMapper {
    
    @Mapping(target = "buildingID", ignore = true)
    @Mapping(target = "emissionFactorID", ignore = true)
    @Mapping(target = "records", ignore = true)
    EmissionActivityDTO toDTO(EmissionActivityEntity emissionActivityEntity);
    
    @Mapping(target = "building", ignore = true)
    @Mapping(target = "emissionFactorEntity", ignore = true)
    EmissionActivityEntity createNewActivity(EmissionActivityDTO dto);

    @Mapping(target = "building", source = "building")
    @Mapping(target = "building.emissionActivities", ignore = true)
    @Mapping(target = "emissionFactor", source = "emissionFactorEntity")
    @Mapping(target = "emissionFactor.isDirectEmission", source = "emissionFactorEntity.directEmission")
    @Mapping(target = "emissionFactor.emissionSourceDTO", source = "emissionFactorEntity.source")
    @Mapping(target = "emissionFactor.energyConversionDTO", source = "emissionFactorEntity.energyConversion")
    @Mapping(target = "records", ignore = true)
    EmissionActivityDetailsDTO toDetailsDTO(EmissionActivityEntity entity);

    @Mapping(target = "emissionFactorEntity", ignore = true)
    @Mapping(target = "building", ignore = true)
    @Mapping(target = "records", ignore = true)
    EmissionActivityEntity updateActivity(EmissionActivityDTO dto);
}
