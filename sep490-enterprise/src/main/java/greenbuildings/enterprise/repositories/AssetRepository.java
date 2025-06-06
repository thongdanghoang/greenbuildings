package greenbuildings.enterprise.repositories;

import greenbuildings.commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.AssetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AssetRepository
        extends AbstractBaseRepository<AssetEntity>, EntityGraphBasedRepository<AssetEntity>, JpaSpecificationExecutor<AssetEntity> {
    
    @Query("""
            FROM AssetEntity assets
            WHERE (:buildingId IS NULL OR assets.building.id = :buildingId)
            AND ((assets.tenant.id IS NOT NULL) OR (assets.enterprise.id IS NOT NULL AND assets.enterprise.id = :enterpriseId))
            AND (assets.disabled = false OR assets.id = :excludeId)
            """)
    List<AssetEntity> selectableByEnterpriseId(UUID enterpriseId, UUID buildingId, UUID excludeId);
    
    @Query("""
            FROM AssetEntity assets
            WHERE (:buildingId IS NULL OR assets.building.id = :buildingId)
            AND ((assets.tenant.id IS NOT NULL AND assets.tenant.id = :tenantId AND assets.disabled = false) OR (:excludeId IS NOT NULL AND assets.id = :excludeId))
            """)
    List<AssetEntity> selectableByTenantId(UUID tenantId, UUID buildingId, UUID excludeId);
    
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
    
    @Query("""
            FROM AssetEntity root
            WHERE root.disabled = false
            AND (
                (root.tenant.id IS NOT NULL AND root.tenant.id = :organizationId)
                OR (root.enterprise.id IS NOT NULL AND root.enterprise.id = :organizationId)
            )
            AND (
                :keyword IS NULL
                OR LOWER(root.name) LIKE CONCAT('%', LOWER(:keyword), '%')
                OR LOWER(root.description) LIKE CONCAT('%', LOWER(:keyword), '%')
                OR LOWER(root.building.name) LIKE CONCAT('%', LOWER(:keyword), '%')
            )
            AND (root.building.id in :buildings)
            """)
    Page<AssetEntity> search(Pageable pageable, UUID organizationId, String keyword, List<UUID> buildings);
    
    List<AssetEntity> findByBuildingIdAndDisabled(UUID buildingId, boolean disabled);
    
    @Query(
            """
                    SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END
                    FROM AssetEntity a
                    WHERE a.code LIKE :code
                    AND (a.enterprise.id = :organizationId OR a.tenant.id = :organizationId)
                    """
    )
    boolean existsAllByCodeEqualsAndOrganizationId(String code, UUID organizationId);
    
    @Query(
            """
                    SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END
                    FROM AssetEntity a
                    WHERE a.code LIKE :code
                    AND (a.enterprise.id = :organizationId OR a.tenant.id = :organizationId)
                    AND (a.id != :existId)
                    """
    )
    boolean existsAllByCodeEqualsAndOrganizationId(String code, UUID organizationId, UUID existId);
}
