package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.springfw.impl.securities.UserContextData;
import greenbuildings.enterprise.dtos.AssetSearchCriteria;
import greenbuildings.enterprise.entities.AssetEntity;
import greenbuildings.enterprise.events.BuildingGroupUnlinkedEvent;
import greenbuildings.enterprise.repositories.AssetRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRecordRepository;
import greenbuildings.enterprise.services.AssetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Throwable.class)
public class AssetServiceImpl implements AssetService {
    
    private final AssetRepository assetRepository;
    private final EmissionActivityRecordRepository recordRepository;
    
    @Override
    public AssetEntity updateAsset(AssetEntity asset) {
        var oldBuilding = assetRepository.getReferenceById(asset.getId()).getBuilding();
        var newBuilding = asset.getBuilding();
        
        var isMoveFromBuildingMoveToAny = Objects.nonNull(oldBuilding);
        var isMoveToDifferentBuilding = Objects.isNull(newBuilding)
                                        || (isMoveFromBuildingMoveToAny && !Objects.equals(oldBuilding.getId(), newBuilding.getId()));
        
        if (isMoveFromBuildingMoveToAny
            && isMoveToDifferentBuilding
            && recordRepository.existsByAssetId((asset.getId())) // side effect
        ) {
            var newAssetEntity = AssetEntity.clone(asset);
            asset.setBuilding(oldBuilding);
            archiveAsset(asset);
            assetRepository.save(asset);
            return assetRepository.save(newAssetEntity);
        }
        return assetRepository.save(asset);
    }
    
    @Override
    public AssetEntity createAsset(AssetEntity assetEntity) {
        return assetRepository.save(assetEntity);
    }
    
    @Override
    public void deleteAsset(UUID id) {
        if (recordRepository.existsByAssetId(id)) {
            assetRepository.findById(id).ifPresent(this::archiveAsset);
            return;
        }
        assetRepository.deleteById(id);
    }
    
    private void archiveAsset(AssetEntity asset) {
        asset.setName("[Archived] %s".formatted(asset.getName()));
        asset.setDisabled(true);
        assetRepository.save(asset);
    }
    
    @Transactional(readOnly = true)
    @Override
    public Page<AssetEntity> search(SearchCriteriaDTO<AssetSearchCriteria> searchCriteria, UUID organizationId) {
        var pageable = CommonMapper.toPageableFallbackSortToLastModifiedDate(searchCriteria.page(), searchCriteria.sort());
        return assetRepository.findAllByOrganizationId(searchCriteria.criteria().criteria(), pageable, organizationId);
    }
    
    @Transactional(readOnly = true)
    @Override
    public AssetEntity getById(UUID id) {
        return assetRepository.getWithGraph(
                id,
                AssetEntity.class,
                new String[]{AssetEntity.Fields.building}
                                           );
    }
    
    @Override
    public List<AssetEntity> selectableByBuildingId(UserContextData userContext, UUID buildingId, UUID excludeId) {
        if (userContext.getEnterpriseId().isPresent()) {
            return assetRepository.selectableByEnterpriseId(userContext.getEnterpriseId().get(), buildingId, excludeId);
        }
        return assetRepository.selectableByTenantId(userContext.getTenantId().orElseThrow(), buildingId, excludeId);
    }
    
    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void handleBuildingGroupUnlinkedEvent(BuildingGroupUnlinkedEvent event) {
        var assets = assetRepository.findByBuildingIdAndDisabled(event.getBuildingId(), false);
        assets.forEach(asset -> asset.setBuilding(null));
        assetRepository.saveAll(assets);
    }
}
