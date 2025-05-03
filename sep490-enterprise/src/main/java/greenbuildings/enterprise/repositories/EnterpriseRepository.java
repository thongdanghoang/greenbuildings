package greenbuildings.enterprise.repositories;

import commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.EnterpriseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnterpriseRepository extends AbstractBaseRepository<EnterpriseEntity> {
    
    Optional<EnterpriseEntity> findByEnterpriseEmail(String email);
    
    @Query("""
            SELECT e FROM EnterpriseEntity e
            WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :criteria, '%'))
               OR LOWER(e.enterpriseEmail) LIKE LOWER(CONCAT('%', :criteria, '%')) 
               OR LOWER(e.hotline) LIKE LOWER(CONCAT('%', :criteria, '%'))
            """)
    Page<EnterpriseEntity> findByCriteria(String criteria, Pageable pageable);
    
    @Query("""
        SELECT e
            FROM EnterpriseEntity e
            JOIN BuildingEntity b on b.enterprise.id = e.id
            WHERE b.id = :id
    """)
    Optional<EnterpriseEntity> findByBuidingId(UUID id);
}
