package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.dtos.TenantDTO;
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

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {
    
    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;
    
    @Override
    @Transactional
    public TenantEntity create(TenantDTO dto) {
        TenantEntity entity = tenantMapper.toEntity(dto);
        return tenantRepository.save(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<TenantEntity> findById(UUID id) {
        return tenantRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TenantEntity> findAll() {
        return tenantRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<TenantEntity> findAll(Pageable pageable) {
        return tenantRepository.findAll(pageable);
    }
    
    @Override
    @Transactional
    public TenantEntity update(UUID id, TenantDTO dto) {
        TenantEntity existingEntity = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + id));
        
        tenantMapper.partialUpdate(dto, existingEntity);
        return tenantRepository.save(existingEntity);
    }
    
    @Override
    @Transactional
    public void delete(UUID id) {
        tenantRepository.deleteById(id);
    }
} 