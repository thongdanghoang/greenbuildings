package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.EmissionFactorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmissionFactorRepository extends JpaRepository<EmissionFactorEntity, UUID> {
    List<EmissionFactorEntity> findBySourceId(UUID sourceId);
}
