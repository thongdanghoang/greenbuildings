package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.entities.AssetEntity;
import greenbuildings.enterprise.repositories.AssetRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRecordRepository;
import greenbuildings.enterprise.services.AssetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
    public AssetEntity updateAsset(AssetEntity target) {
        var buildingFrom = assetRepository.getReferenceById(target.getId()).getBuilding();
        var buildingTo = target.getBuilding();
        
        var isMoveFromBuildingMoveToAny = Objects.nonNull(buildingFrom);
        var isMoveToDifferentBuilding = Objects.isNull(buildingTo) || !Objects.equals(buildingFrom.getId(), buildingTo.getId());
        
        if (isMoveFromBuildingMoveToAny
            && isMoveToDifferentBuilding
            && recordRepository.existsByAssetId((target.getId())) // side effect
        ) {
            var newAssetEntity = AssetEntity.clone(target);
            target.setName("Archived asset with name: %s".formatted(target.getName()));
            assetRepository.deleteById(target.getId()); // use type better than delete?
            return assetRepository.save(newAssetEntity);
        }
        return assetRepository.save(target);
    }
    
    @Override
    public AssetEntity createAsset(AssetEntity assetEntity) {
        return assetRepository.save(assetEntity);
    }
    
    @Override
    public void deleteAsset(UUID id) {
        assetRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    @Override
    public Page<AssetEntity> search(Pageable pageable, UUID organizationId) {
        return assetRepository.findAllByOrganizationId(pageable, organizationId);
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
    public List<AssetEntity> selectableByOrganizationId(UUID organizationId, UUID buildingId) {
        return assetRepository.selectableByOrganizationId(organizationId, buildingId);
    }
}
