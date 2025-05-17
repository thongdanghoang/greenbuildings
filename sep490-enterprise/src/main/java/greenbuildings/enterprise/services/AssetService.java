package greenbuildings.enterprise.services;

import greenbuildings.enterprise.entities.AssetEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AssetService {
    
    AssetEntity saveAsset(AssetEntity assetEntity);
    
    void deleteAsset(UUID id);
    
    Page<AssetEntity> search(Pageable pageable);
    
    AssetEntity getById(UUID id);
    
    List<AssetEntity> selectableByOrganizationId(UUID organizationId);
    
}
