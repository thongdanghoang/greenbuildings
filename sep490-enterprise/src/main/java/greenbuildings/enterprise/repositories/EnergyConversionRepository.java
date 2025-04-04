package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.EnergyConversionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EnergyConversionRepository extends JpaRepository<EnergyConversionEntity, UUID> {

    @Query("""
            SELECT e.id
            FROM EnergyConversionEntity e
            WHERE (LOWER(e.fuel.nameEN) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(e.fuel.nameVN) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(e.fuel.nameZH) LIKE LOWER(CONCAT('%', :name, '%')))
            """
    )
    Page<UUID> findByName(String name, Pageable pageable);
}
