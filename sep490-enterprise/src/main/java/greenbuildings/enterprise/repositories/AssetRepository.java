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
            WHERE assets.building.enterprise.id = :organizationId
            OR assets.building.id
            IN (
                SELECT groups.building.id
                FROM BuildingGroupEntity groups
                WHERE groups.tenant.id = :organizationId
            )
            """)
    List<AssetEntity> selectableByOrganizationId(UUID organizationId);
}
