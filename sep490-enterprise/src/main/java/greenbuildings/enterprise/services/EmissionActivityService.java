package greenbuildings.enterprise.services;


import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionActivityCriteria;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import org.springframework.data.domain.Page;

public interface EmissionActivityService {
    Page<EmissionActivityEntity> search(SearchCriteriaDTO<EmissionActivityCriteria> searchCriteria);
    
    EmissionActivityEntity add(EmissionActivityEntity entity);
}
