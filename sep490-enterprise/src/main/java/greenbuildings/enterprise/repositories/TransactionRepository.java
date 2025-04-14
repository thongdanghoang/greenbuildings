package greenbuildings.enterprise.repositories;

import commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends AbstractBaseRepository<TransactionEntity> {
    List<TransactionEntity> findAllByBuilding_IdAndEnterprise_Id(UUID buildingId, UUID enterpriseId);

    Page<TransactionEntity> findAllByBuilding_IdAndEnterprise_Id(UUID buildingId, UUID enterpriseId, Pageable pageable);
}
