package greenbuildings.enterprise.repositories;

import greenbuildings.commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.WalletEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletRepository extends AbstractBaseRepository<WalletEntity> {
    WalletEntity findByEnterpriseId(UUID enterpriseId);
}
