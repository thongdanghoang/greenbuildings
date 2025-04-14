package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.BuildingGroupCriteria;
import greenbuildings.enterprise.dtos.BuildingGroupDTO;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BuildingGroupService {
    BuildingGroupEntity create(BuildingGroupDTO dto);
    
    Optional<BuildingGroupEntity> findById(UUID id);
    
    List<BuildingGroupEntity> findAll();
    
    Page<BuildingGroupEntity> findAll(Pageable pageable);
    
    BuildingGroupEntity update(UUID id, BuildingGroupDTO dto);
    
    void delete(UUID id);
    
    List<BuildingGroupEntity> findByBuildingId(UUID buildingId);
    
    List<BuildingGroupEntity> findByTenantId(UUID tenantId);
    
    Page<BuildingGroupEntity> search(SearchCriteriaDTO<BuildingGroupCriteria> searchCriteria);
}