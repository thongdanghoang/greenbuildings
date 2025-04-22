package greenbuildings.enterprise.services;

import greenbuildings.enterprise.dtos.TenantDTO;
import greenbuildings.enterprise.entities.TenantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantService {
    TenantEntity create(TenantEntity dto);
    
    Optional<TenantEntity> findById(UUID id);
    
    List<TenantEntity> findAll();
    
    Page<TenantEntity> findAll(Pageable pageable);
    
    TenantEntity update(UUID id, TenantDTO dto);
    
    void delete(UUID id);
} 