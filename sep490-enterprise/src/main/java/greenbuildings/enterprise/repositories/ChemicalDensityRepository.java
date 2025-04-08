package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.ChemicalDensityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChemicalDensityRepository extends JpaRepository<ChemicalDensityEntity, UUID> {
} 