package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.FuelCriteriaDTO;
import greenbuildings.enterprise.entities.FuelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface FuelService {
    Set<FuelEntity> findAll();
    Page<FuelEntity> search(SearchCriteriaDTO<FuelCriteriaDTO> searchCriteria, Pageable pageable);
    void createOrUpdate(FuelEntity fuelEntity);
    Optional<FuelEntity> findById(UUID id);
}
