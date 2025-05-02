package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.SearchTenantCriteria;
import greenbuildings.enterprise.dtos.TenantDTO;
import greenbuildings.enterprise.dtos.TenantDetailDTO;
import greenbuildings.enterprise.dtos.TenantTableView;
import greenbuildings.enterprise.entities.TenantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantService {
    TenantEntity create(TenantEntity dto);
    
    Optional<TenantEntity> findById(UUID id);
    
    Optional<TenantDetailDTO> getTenantDetail(UUID id);

    TenantDetailDTO updateTenantDetail(UUID id, TenantDetailDTO detailDTO);

    List<TenantEntity> findAll();
    
    Page<TenantEntity> findAll(Pageable pageable);
    
    TenantEntity update(UUID id, TenantDTO dto);
    
    void delete(UUID id);
    
    Page<TenantTableView> search(UUID enterpriseID, SearchCriteriaDTO<SearchTenantCriteria> searchCriteria, Pageable pageable);
    
    List<TenantEntity> findByBuildingId(UUID buildingId);
}
