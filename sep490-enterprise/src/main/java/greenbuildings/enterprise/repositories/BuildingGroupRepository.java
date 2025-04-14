package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.BuildingGroupEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BuildingGroupRepository extends JpaRepository<BuildingGroupEntity, UUID> {
    List<BuildingGroupEntity> findByBuildingId(UUID buildingId);
    
    List<BuildingGroupEntity> findByTenantId(UUID tenantId);
    
    
    Page<BuildingGroupEntity> findAllByBuildingIdAndNameContainingIgnoreCase(@NotNull UUID uuid, String s, Pageable pageable);
}
