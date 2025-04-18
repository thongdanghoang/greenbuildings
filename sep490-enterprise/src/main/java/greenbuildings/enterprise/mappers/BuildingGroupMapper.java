package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.BuildingGroupDTO;
import greenbuildings.enterprise.dtos.CreateBuildingGroupDTO;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import greenbuildings.enterprise.mappers.decorators.BuildingGroupMapperDecorator;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
@DecoratedWith(BuildingGroupMapperDecorator.class)
public interface BuildingGroupMapper {
    BuildingGroupEntity toEntity(BuildingGroupDTO dto);
    
    @Mapping(target = "building", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "emissionActivities", ignore = true)
    BuildingGroupDTO toDTO(BuildingGroupEntity entity);
    
    @Mapping(target = "building", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "emissionActivities", ignore = true)
    BuildingGroupDTO toDetailDTO(BuildingGroupEntity entity);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BuildingGroupEntity partialUpdate(BuildingGroupDTO dto, @MappingTarget BuildingGroupEntity entity);

    BuildingGroupEntity toEntity(CreateBuildingGroupDTO dto);
} 