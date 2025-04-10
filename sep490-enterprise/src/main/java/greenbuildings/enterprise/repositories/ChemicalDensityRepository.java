package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.ChemicalDensityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChemicalDensityRepository extends JpaRepository<ChemicalDensityEntity, UUID> {

    @Query("""
            SELECT e.id
            FROM ChemicalDensityEntity e
            WHERE (LOWER(e.chemicalFormula) LIKE LOWER(CONCAT('%', :name, '%')))
            """
    )
    Page<UUID> findByName(String name, Pageable pageable);
} 