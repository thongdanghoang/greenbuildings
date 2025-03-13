package greenbuildings.enterprise.services;

import greenbuildings.enterprise.entities.CreditPackageEntity;
import greenbuildings.enterprise.entities.CreditPackageVersionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface CreditPackageService {
    
    List<CreditPackageVersionEntity> findAll();
    
    Optional<CreditPackageVersionEntity> findById(UUID id);

    void createOrUpdate(CreditPackageVersionEntity creditPackageVersionEntity);

    void deletePackage(UUID packageId);

    Page<CreditPackageVersionEntity> search(Pageable pageable);
}
