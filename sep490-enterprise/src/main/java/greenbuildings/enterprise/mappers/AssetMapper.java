package greenbuildings.enterprise.mappers;

import greenbuildings.commons.api.views.SelectableItem;
import greenbuildings.enterprise.dtos.assets.AssetDTO;
import greenbuildings.enterprise.entities.AssetEntity;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.views.assets.AssetView;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.Optional;
import java.util.UUID;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class AssetMapper {
    
    public abstract AssetView toView(AssetEntity source);
    
    @Mapping(target = "building", source = "buildingId")
    public abstract AssetEntity toEntity(AssetDTO source);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void partialUpdate(AssetEntity source, @MappingTarget AssetEntity target);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    public abstract void fullUpdate(AssetEntity source, @MappingTarget AssetEntity target);
    
    @Mapping(target = "value", source = "id")
    @Mapping(target = "label", source = "name")
    public abstract SelectableItem<UUID> toSelectableItem(AssetEntity source);
    
    BuildingEntity toBuildingEntity(UUID buildingId) {
        return Optional.ofNullable(buildingId)
                       .map(BuildingEntity::of)
                       .orElse(null);
    }
} 