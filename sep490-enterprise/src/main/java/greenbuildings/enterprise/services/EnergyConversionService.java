package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.FuelCriteriaDTO;
import greenbuildings.enterprise.entities.EnergyConversionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnergyConversionService {
    List<EnergyConversionEntity> findAll();
    Page<EnergyConversionEntity> search(SearchCriteriaDTO<FuelCriteriaDTO> searchCriteria, Pageable pageable);
    Optional<EnergyConversionEntity> findById(UUID id);
    void createOrUpdate(EnergyConversionEntity energyConversionEntity);
}
