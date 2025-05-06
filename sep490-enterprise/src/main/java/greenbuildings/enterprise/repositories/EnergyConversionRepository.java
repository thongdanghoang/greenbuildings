package greenbuildings.enterprise.repositories;

import commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.EnergyConversionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnergyConversionRepository extends AbstractBaseRepository<EnergyConversionEntity> {

    @Query("""
            SELECT e.id
            FROM EnergyConversionEntity e
            WHERE (LOWER(e.fuel.nameEN) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(e.fuel.nameVN) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(e.fuel.nameZH) LIKE LOWER(CONCAT('%', :name, '%')))
            """
    )
    Page<UUID> findByName(String name, Pageable pageable);

    @Query("""
            SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END
            FROM EnergyConversionEntity e
            WHERE LOWER(e.fuel.nameVN) = LOWER(:nameVN)
               OR LOWER(e.fuel.nameEN) = LOWER(:nameEN)
               OR LOWER(e.fuel.nameZH) = LOWER(:nameZH)
            """
    )
    boolean existsByName(String nameVN, String nameEN, String nameZH);

    @Query("""
            SELECT e
            FROM EnergyConversionEntity e
            WHERE LOWER(e.fuel.nameVN) = LOWER(:nameVN)
              OR LOWER(e.fuel.nameEN) = LOWER(:nameEN)
              OR LOWER(e.fuel.nameZH) = LOWER(:nameZH)
            """
    )
    Optional<EnergyConversionEntity> findByName(String nameVN, String nameEN, String nameZH);
}
