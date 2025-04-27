package greenbuildings.enterprise.services;

import greenbuildings.enterprise.dtos.EnterpriseDetailDTO;
import greenbuildings.enterprise.entities.EnterpriseEntity;

import java.util.UUID;

public interface EnterpriseService {
    
    UUID createEnterprise(EnterpriseEntity enterprise);
    
    EnterpriseEntity getById(UUID id);
    
    // Get enterprise detail by id
    EnterpriseDetailDTO getEnterpriseDetail(UUID id);

    // Update enterprise detail
    void updateEnterpriseDetail(EnterpriseDetailDTO dto);
}
