package greenbuildings.enterprise.services;


import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionActivityCriteria;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import org.springframework.data.domain.Page;

import java.util.Set;
import java.util.UUID;

public interface EmissionActivityService {
    Page<EmissionActivityEntity> search(SearchCriteriaDTO<EmissionActivityCriteria> searchCriteria);
    
    EmissionActivityEntity add(EmissionActivityEntity entity);
    
    void deleteActivities(Set<UUID> ids);
}
