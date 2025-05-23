package greenbuildings.enterprise.services;

import greenbuildings.commons.springfw.impl.securities.UserContextData;
import greenbuildings.enterprise.entities.AssetEntity;
import greenbuildings.enterprise.events.BuildingGroupUnlinkedEvent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AssetService {
    
    AssetEntity updateAsset(AssetEntity target);
    
    AssetEntity createAsset(AssetEntity assetEntity);
    
    void deleteAsset(UUID id);
    
    Page<AssetEntity> search(Pageable pageable, UUID organizationId);
    
    AssetEntity getById(UUID id);
    
    List<AssetEntity> selectableByBuildingId(UserContextData userContext, UUID buildingId, UUID excludeId);
    
    void handleBuildingGroupUnlinkedEvent(BuildingGroupUnlinkedEvent event);
    
}
