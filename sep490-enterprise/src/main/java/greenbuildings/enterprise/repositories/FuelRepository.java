package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.FuelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FuelRepository extends JpaRepository<FuelEntity, UUID> {
}
