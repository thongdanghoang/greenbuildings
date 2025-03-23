package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.FuelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FuelRepository extends JpaRepository<FuelEntity, UUID> {
    @Query("""
            SELECT f.id
            FROM FuelEntity f
            WHERE (LOWER(f.nameEN) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(f.nameVN) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(f.nameZH) LIKE LOWER(CONCAT('%', :name, '%')))
            """
    )
    Page<UUID> findByName(String name, Pageable pageable);
}
