package greenbuildings.enterprise.repositories;

import commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BuildingGroupRepository extends AbstractBaseRepository<BuildingGroupEntity> {
    
    boolean existsByNameIgnoreCaseAndBuildingId(String name, UUID buildingId);
    
    List<BuildingGroupEntity> findByBuildingId(UUID buildingId);
    
    List<BuildingGroupEntity> findByTenantId(UUID tenantId);
    
    boolean existsByBuildingIdAndTenantEmail(UUID id, String tenantEmail);
    
    @Query(value = """
            SELECT g
            FROM BuildingGroupEntity g
            WHERE g.building.id = :buildingId
              AND g.tenant IS NULL
              AND NOT EXISTS (
                SELECT 1
                FROM InvitationEntity i
                WHERE i.buildingGroup.id = g.id
                  AND i.status = 'PENDING'
            )
            """)
    List<BuildingGroupEntity> findAvailableGroupForInvite(UUID buildingId);
    
    Page<BuildingGroupEntity> findAllByBuildingIdAndNameContainingIgnoreCase(@NotNull UUID uuid, String s, Pageable pageable);
}
