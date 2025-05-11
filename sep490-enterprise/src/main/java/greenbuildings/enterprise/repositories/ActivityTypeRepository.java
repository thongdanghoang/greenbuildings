package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.ActivityTypeEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityTypeEntity, UUID> {
    List<ActivityTypeEntity> findByBuildingId(UUID buildingId);
    
    @Query("""
            SELECT e.id
            FROM ActivityTypeEntity e
            WHERE e.building.id = :buildingId
                  AND (LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))
            """
    )
    Page<UUID> findByName(UUID buildingId, String name, Pageable pageable);
    
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM ActivityTypeEntity b WHERE b.name = :name and b.building.id = :buildingId")
    boolean existsByNameActivityTypeInTenant(@NotNull UUID buildingId, @NotBlank String name);
    
    ActivityTypeEntity findByNameIgnoreCaseAndBuildingId(String name, UUID buildingId);
} 