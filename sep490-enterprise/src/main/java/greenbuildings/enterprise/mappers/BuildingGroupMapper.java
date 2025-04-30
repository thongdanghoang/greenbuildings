package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.BuildingGroupDTO;
import greenbuildings.enterprise.dtos.CreateBuildingGroupDTO;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import greenbuildings.enterprise.mappers.decorators.BuildingGroupMapperDecorator;
import greenbuildings.enterprise.views.buildings.details.TenantView;
import org.mapstruct.BeanMapping;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

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
    
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "emissionActivities", ignore = true)
    @Mapping(target = "building.buildingGroups", ignore = true)
    @Mapping(target = "building.subscriptionDTO", ignore = true)
    BuildingGroupDTO toDTOWithBuilding(BuildingGroupEntity entity);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BuildingGroupEntity partialUpdate(BuildingGroupDTO dto, @MappingTarget BuildingGroupEntity entity);

    BuildingGroupEntity toEntity(CreateBuildingGroupDTO dto);
    
    @Mapping(target ="id", source = "tenant.id")
    @Mapping(target ="name", source = "tenant.name")
    @Mapping(target ="buildingId", source = "building.id")
    @Mapping(target ="buildingGroupId", source = "id")
    @Mapping(target ="buildingGroupName", source = "name")
    TenantView toTenantView(BuildingGroupEntity source);
} 