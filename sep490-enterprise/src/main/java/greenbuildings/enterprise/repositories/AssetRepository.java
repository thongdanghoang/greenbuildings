package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.AssetEntity;

import commons.springfw.impl.repositories.AbstractBaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AssetRepository
        extends AbstractBaseRepository<AssetEntity>, EntityGraphBasedRepository<AssetEntity> {
    
    @Query("""
            FROM AssetEntity assets
            WHERE (:buildingId IS NULL OR assets.building.id = :buildingId)
            AND (
                assets.building.enterprise.id = :organizationId
                OR assets.building.id
                IN (
                    SELECT groups.building.id
                    FROM BuildingGroupEntity groups
                    WHERE groups.tenant.id = :organizationId
                )
            )
            """)
    List<AssetEntity> selectableByOrganizationId(UUID organizationId, UUID buildingId);
    
    @Query("""
                SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
                  FROM AssetEntity a
                 WHERE a.id = :id
                   AND EXISTS (
                       SELECT 1
                         FROM EmissionActivityEntity eae
                        WHERE eae.id = :activityId
                          AND eae.building.id = a.building.id
                   )
            """)
    boolean existsByIdAndActivityId(UUID id, UUID activityId);
    
}
