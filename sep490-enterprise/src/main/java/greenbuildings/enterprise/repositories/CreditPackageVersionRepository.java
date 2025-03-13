package greenbuildings.enterprise.repositories;
import commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.CreditPackageEntity;
import greenbuildings.enterprise.entities.CreditPackageVersionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CreditPackageVersionRepository extends AbstractBaseRepository<CreditPackageVersionEntity> {
    @Query("""
            SELECT c
            FROM CreditPackageVersionEntity c
            WHERE c.active = true
            and c.creditPackageEntity.id = :creditPackageId
            """)
    CreditPackageVersionEntity findActiveTrueAndId(UUID creditPackageId);

    @Query("""
            SELECT c
            FROM CreditPackageVersionEntity c
            WHERE c.active = true
            and c.id = :creditPackageVersionId
            """)
    CreditPackageVersionEntity findActiveTrueAndIdVersion(UUID creditPackageVersionId);
}
