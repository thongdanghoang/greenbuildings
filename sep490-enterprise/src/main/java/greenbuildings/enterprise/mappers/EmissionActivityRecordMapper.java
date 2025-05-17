package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.EmissionActivityRecordDTO;
import greenbuildings.enterprise.dtos.NewEmissionActivityRecordDTO;
import greenbuildings.enterprise.entities.AssetEntity;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.mappers.decorators.EmissionActivityRecordMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.Optional;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@DecoratedWith(EmissionActivityRecordMapperDecorator.class)
public interface EmissionActivityRecordMapper {
    
    @Mapping(target = "assetId", source = "asset.id")
    EmissionActivityRecordDTO toDTO(EmissionActivityRecordEntity emissionActivityEntity);

    @Mapping(target = "emissionActivity.id", source = "activityId")
    @Mapping(target = "asset", source = "assetId")
    EmissionActivityRecordEntity newToEntity(NewEmissionActivityRecordDTO emissionActivityRecordDTO);
    
    default AssetEntity toAssetEntity(UUID assetId) {
        return Optional.ofNullable(assetId)
                       .map(AssetEntity::of)
                       .orElse(null);
    }
}
