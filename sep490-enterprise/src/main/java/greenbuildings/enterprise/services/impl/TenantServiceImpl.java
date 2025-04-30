package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.SearchTenantCriteria;
import greenbuildings.enterprise.dtos.TenantDTO;
import greenbuildings.enterprise.dtos.TenantDetailDTO;
import greenbuildings.enterprise.dtos.TenantTableView;
import greenbuildings.enterprise.entities.TenantEntity;
import greenbuildings.enterprise.mappers.TenantMapper;
import greenbuildings.enterprise.repositories.TenantRepository;
import greenbuildings.enterprise.services.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class TenantServiceImpl implements TenantService {
    
    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;
    
    @Override
    public TenantEntity create(TenantEntity tenant) {
        return tenantRepository.save(tenant);
    }
    
    @Override
    public Optional<TenantEntity> findById(UUID id) {
        return tenantRepository.findById(id);
    }
    
    @Override
    public Optional<TenantDetailDTO> getTenantDetail(UUID id) {
        return tenantRepository.findById(id)
                .map(tenantMapper::toDetailDTO);
    }

    @Override
    public TenantDetailDTO updateTenantDetail(UUID id, TenantDetailDTO detailDTO) {
        TenantEntity existingEntity = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + id));

        // Update fields from detailDTO
        existingEntity.setName(detailDTO.name());
        existingEntity.setEmail(detailDTO.email());
        existingEntity.setHotline(detailDTO.hotline());

        // Save and return updated entity
        TenantEntity savedEntity = tenantRepository.save(existingEntity);
        return tenantMapper.toDetailDTO(savedEntity);
    }

    @Override
    public List<TenantEntity> findAll() {
        return tenantRepository.findAll();
    }
    
    @Override
    public Page<TenantEntity> findAll(Pageable pageable) {
        var tenants = tenantRepository.findAllProjectToUUID(pageable);
        var results = tenantRepository
                .findAllByIdIn(tenants.toSet())
                .stream()
                .collect(Collectors.toMap(TenantEntity::getId, Function.identity()));
        return tenants.map(results::get);
    }
    
    @Override
    public TenantEntity update(UUID id, TenantDTO dto) {
        TenantEntity existingEntity = tenantRepository.findById(id)
                                                      .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + id));
        
        tenantMapper.partialUpdate(dto, existingEntity);
        var tenantEntityId = tenantRepository.save(existingEntity).getId();
        return tenantRepository.findById(tenantEntityId).orElseThrow();
    }
    
    @Override
    public void delete(UUID id) {
        tenantRepository.deleteById(id);
    }
    
    @Override
    public Page<TenantTableView> search(SearchCriteriaDTO<SearchTenantCriteria> searchCriteria, Pageable pageable) {
        UUID enterpriseID = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
        return tenantRepository.findByEnterpriseId(enterpriseID, searchCriteria.criteria().email(), pageable).map(TenantTableView::fromEntity);
    }
    
    @Override
    public List<TenantEntity> findByBuildingId(UUID buildingId) {
        return List.of();
    }
}
