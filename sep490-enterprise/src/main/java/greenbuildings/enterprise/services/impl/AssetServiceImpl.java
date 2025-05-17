package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.entities.AssetEntity;
import greenbuildings.enterprise.repositories.AssetRepository;
import greenbuildings.enterprise.services.AssetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Throwable.class)
public class AssetServiceImpl implements AssetService {
    
    private final AssetRepository assetRepository;
    
    @Override
    public AssetEntity saveAsset(AssetEntity assetEntity) {
        return assetRepository.save(assetEntity);
    }
    
    @Override
    public void deleteAsset(UUID id) {
        assetRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    @Override
    public Page<AssetEntity> search(Pageable pageable) {
        return assetRepository.findAll(pageable);
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
    public List<AssetEntity> selectableByOrganizationId(UUID organizationId) {
        return assetRepository.selectableByOrganizationId(organizationId);
    }
}
