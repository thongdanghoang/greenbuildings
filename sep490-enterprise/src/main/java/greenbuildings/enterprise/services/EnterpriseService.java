package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.EnterpriseDetailDTO;
import greenbuildings.enterprise.dtos.EnterpriseSearchCriteria;
import greenbuildings.enterprise.entities.EnterpriseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EnterpriseService {
    
    UUID createEnterprise(EnterpriseEntity enterprise);
    
    EnterpriseEntity getById(UUID id);
    
    // Get enterprise detail by id
    EnterpriseDetailDTO getEnterpriseDetail(UUID id);

    // Update enterprise detail
    void updateEnterpriseDetail(EnterpriseDetailDTO dto);
    
    Page<EnterpriseEntity> search(SearchCriteriaDTO<EnterpriseSearchCriteria> searchCriteria, Pageable pageable);
}
