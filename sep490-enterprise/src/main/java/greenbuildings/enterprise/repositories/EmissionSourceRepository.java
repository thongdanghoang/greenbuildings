package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.EmissionSourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface EmissionSourceRepository extends JpaRepository<EmissionSourceEntity, UUID> {

    @Query("""
            SELECT e.id
            FROM EmissionSourceEntity e
            WHERE (LOWER(e.nameEN) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(e.nameVN) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(e.nameZH) LIKE LOWER(CONCAT('%', :name, '%')))
            """
    )
    Page<UUID> findByName(String name, Pageable pageable);
    @Query("""
            SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END
            FROM EmissionSourceEntity e
            WHERE LOWER(e.nameVN) = LOWER(:nameVN)
               OR LOWER(e.nameEN) = LOWER(:nameEN)
               OR LOWER(e.nameZH) = LOWER(:nameZH)
            """
    )
    boolean existsByName(String nameVN, String nameEN, String nameZH);

    @Query("""
            SELECT e
            FROM EmissionSourceEntity e
            WHERE LOWER(e.nameVN) = LOWER(:nameVN)
              OR LOWER(e.nameEN) = LOWER(:nameEN)
              OR LOWER(e.nameZH) = LOWER(:nameZH)
            """
    )
    EmissionSourceEntity findByName(String nameVN, String nameEN, String nameZH);
}
