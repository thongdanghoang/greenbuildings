package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.BuildingGroupCriteria;
import greenbuildings.enterprise.dtos.BuildingGroupDTO;
import greenbuildings.enterprise.dtos.InviteTenantToBuildingGroup;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface BuildingGroupService {
    BuildingGroupEntity create(BuildingGroupDTO dto);
    
    Optional<BuildingGroupEntity> findById(UUID id);
    
    List<BuildingGroupEntity> findAll();
    
    Page<BuildingGroupEntity> findAll(Pageable pageable);
    
    BuildingGroupEntity update(UUID id, BuildingGroupDTO dto);
    
    void delete(Set<UUID> buildingGroupIDs);
    
    List<BuildingGroupEntity> findByBuildingId(UUID buildingId);
    
    Page<BuildingGroupEntity> searchByBuildingIdWithTenant(UUID buildingId, Pageable pageable);
    
    List<BuildingGroupEntity> findByTenantId(UUID tenantId);
    
    Page<BuildingGroupEntity> search(SearchCriteriaDTO<BuildingGroupCriteria> searchCriteria);
    
    List<BuildingGroupEntity> getAvailableBuildingGroups(UUID buildingId);
    
    BuildingGroupEntity create(BuildingGroupEntity buildingGroupEntity);
    
    void inviteTenant(InviteTenantToBuildingGroup dto);
}