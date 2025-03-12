package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.EmissionSourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmissionSourceRepository extends JpaRepository<EmissionSourceEntity, UUID> {

}
