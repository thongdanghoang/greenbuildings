package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.EmissionActivityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface EmissionActivityRepository extends JpaRepository<EmissionActivityEntity, UUID> {
    
    Set<EmissionActivityEntity> findByBuildingId(UUID id);

    Page<EmissionActivityEntity> findAllByBuildingIdAndNameContainingIgnoreCase(UUID buildingId, String activityName,Pageable pageable);
    
    Integer countAllByIdIn(Set<UUID> ids);
    
    default boolean existsAllById(Set<UUID> ids) {
        return countAllByIdIn(ids).equals(ids.size());
    }

    @EntityGraph(value = EmissionActivityEntity.DETAILS_GRAPH)
    Optional<EmissionActivityEntity> findDetailsById(UUID id);
}
