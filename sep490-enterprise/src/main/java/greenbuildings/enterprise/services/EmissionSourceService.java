package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionSourceCriteriaDTO;
import greenbuildings.enterprise.entities.EmissionSourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface EmissionSourceService {
    Set<EmissionSourceEntity> findAll();
    Page<EmissionSourceEntity> search(SearchCriteriaDTO<EmissionSourceCriteriaDTO> searchCriteria, Pageable pageable);
}
