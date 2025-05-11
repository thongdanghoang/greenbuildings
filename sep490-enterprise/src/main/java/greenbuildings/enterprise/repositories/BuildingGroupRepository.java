package greenbuildings.enterprise.repositories;

import commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface BuildingGroupRepository extends AbstractBaseRepository<BuildingGroupEntity> {
    
    boolean existsByNameIgnoreCaseAndBuildingId(String name, UUID buildingId);
    
    List<BuildingGroupEntity> findByBuildingId(UUID buildingId);
    
    @Query("""
            SELECT g.id
            FROM BuildingGroupEntity g
            WHERE g.building.id = :buildingId
            AND g.tenant IS NOT NULL
            """)
    Page<UUID> findByBuildingId(UUID buildingId, Pageable pageable);
    
    @EntityGraph(attributePaths = BuildingGroupEntity.Fields.tenant)
    @Query("SELECT g FROM BuildingGroupEntity g WHERE g.id IN :buildingGroupIDs")
    List<BuildingGroupEntity> fetchTenants(Set<UUID> buildingGroupIDs);
    
    List<BuildingGroupEntity> findByTenantId(UUID tenantId);
    
    boolean existsByBuildingIdAndTenantEmail(UUID id, String tenantEmail);
    
    List<BuildingGroupEntity> findByBuildingIdAndTenantIsNull(UUID buildingId);
    
    Page<BuildingGroupEntity> findAllByBuildingIdAndNameContainingIgnoreCase(@NotNull UUID uuid, String s, Pageable pageable);
    
    boolean existsByIdAndBuildingId(UUID id, UUID buildingId);
}
