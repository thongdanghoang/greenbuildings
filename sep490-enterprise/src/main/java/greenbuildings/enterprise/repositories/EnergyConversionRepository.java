package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.EnergyConversionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EnergyConversionRepository extends JpaRepository<EnergyConversionEntity, UUID> {
}
