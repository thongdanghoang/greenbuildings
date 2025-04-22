package greenbuildings.enterprise.repositories;

import commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.EnterpriseEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnterpriseRepository extends AbstractBaseRepository<EnterpriseEntity> {
    
    Optional<EnterpriseEntity> findByEmail(String email);
    
}
