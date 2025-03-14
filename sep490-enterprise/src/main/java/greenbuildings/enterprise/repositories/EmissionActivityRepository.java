package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.EmissionActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;
import java.util.UUID;

@Repository
public interface EmissionActivityRepository extends JpaRepository<EmissionActivityEntity, UUID> {
    
    Set<EmissionActivityEntity> findByBuildingId(@PathVariable UUID id);
    
}
