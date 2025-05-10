package greenbuildings.enterprise.repositories;

import commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.BuildingEntity;
import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface BuildingRepository extends AbstractBaseRepository<BuildingEntity> {

    Page<BuildingEntity> findByEnterpriseId(UUID enterpriseId, Pageable pageable);
    
    @Query("""
            FROM BuildingEntity building
            JOIN building.subscriptions subscription
            WHERE building.enterprise.id = :enterpriseId
            AND subscription.startDate <= :today
            AND subscription.endDate >= :today
            """)
    List<BuildingEntity> findValidBuildingsByEnterpriseId(UUID enterpriseId, LocalDate today);
    
    @Query("""
            FROM BuildingEntity building
            JOIN building.subscriptions subscription
            WHERE building.id IN :ids
            AND subscription.startDate <= :today
            AND subscription.endDate >= :today
            """)
    List<BuildingEntity> findValidBuildingsIn(Set<UUID> ids, LocalDate today);
    
    Optional<BuildingEntity> findByIdAndEnterpriseId(UUID id, UUID enterpriseId);
    
    @EntityGraph(attributePaths = BuildingEntity.Fields.buildingGroups)
    @Query(value = "SELECT b FROM BuildingEntity b where b.id=:id")
    Optional<BuildingEntity> findByIdWithGraph(@NotNull UUID id);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BuildingEntity b WHERE b.name = :name AND b.enterprise.id = :enterpriseId")
    boolean existsByNameBuildingInEnterprise(@NotBlank String name, UUID enterpriseId);
    
    @Query("""
            SELECT building
            FROM BuildingEntity building
            JOIN building.subscriptions subscription
            WHERE building.enterprise.id = :enterpriseId
            AND subscription.startDate <= :today
            AND subscription.endDate >= :today
            """)
    List<BuildingEntity> getBuildingsByEnterpriseId(UUID enterpriseId, LocalDate today);
}
