package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.ChemicalDensityCriteria;
import greenbuildings.enterprise.entities.ChemicalDensityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ChemicalDensityService {
    Page<ChemicalDensityEntity> search(SearchCriteriaDTO<ChemicalDensityCriteria> searchCriteria, Pageable pageable);
    void createOrUpdate(ChemicalDensityEntity chemicalDensityEntity);
    Optional<ChemicalDensityEntity> findById(UUID id);
    void delete(Set<UUID> typeIds);
}
