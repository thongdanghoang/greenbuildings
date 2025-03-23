package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.FuelCriteriaDTO;
import greenbuildings.enterprise.entities.FuelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface FuelService {
    Set<FuelEntity> findAll();
    Page<FuelEntity> search(SearchCriteriaDTO<FuelCriteriaDTO> searchCriteria, Pageable pageable);
}
