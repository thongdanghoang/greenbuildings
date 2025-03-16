package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.EmissionActivityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface EmissionActivityRepository extends JpaRepository<EmissionActivityEntity, UUID> {
    
    Set<EmissionActivityEntity> findByBuildingId(UUID id);

    Page<EmissionActivityEntity> findAllByBuildingId(UUID id, Pageable pageable);
}
