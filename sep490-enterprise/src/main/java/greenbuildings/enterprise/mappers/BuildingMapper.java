package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.BuildingDTO;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.mappers.decorators.BuildingMapperDecorator;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
@DecoratedWith(BuildingMapperDecorator.class)
public interface BuildingMapper {
    BuildingEntity toEntity(BuildingDTO buildingDTO);

    @Mapping(target = "subscriptionDTO", ignore = true)
    BuildingDTO toDto(BuildingEntity buildingEntity);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BuildingEntity partialUpdate(BuildingDTO buildingDTO, @MappingTarget
    BuildingEntity buildingEntity);
}