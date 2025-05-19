package greenbuildings.enterprise.repositories;

import greenbuildings.commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.CreditPackageEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditPackageRepository extends AbstractBaseRepository<CreditPackageEntity> {

    @Query("""
            SELECT c
            FROM CreditPackageEntity c
            WHERE c.active = true
            """)
    List<CreditPackageEntity> findAllByActiveTrue();
    
}
