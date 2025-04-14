package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.ActivityTypeCriteriaDTO;
import greenbuildings.enterprise.dtos.ActivityTypeDTO;
import greenbuildings.enterprise.entities.ActivityTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ActivityTypeService {
    List<ActivityTypeEntity> findAll();
    
    List<ActivityTypeEntity> findByTenantId(UUID buildingId);
    
    ActivityTypeEntity create(ActivityTypeDTO dto);

    Page<ActivityTypeEntity> search(SearchCriteriaDTO<ActivityTypeCriteriaDTO> searchCriteria, Pageable pageable);

    Optional<ActivityTypeEntity> findById(UUID id);

    void createOrUpdate(ActivityTypeEntity activityTypeEntity);

    void delete(Set<UUID> typeIds);
}
