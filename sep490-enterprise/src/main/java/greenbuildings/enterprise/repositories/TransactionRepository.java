package greenbuildings.enterprise.repositories;

import commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.dtos.NumberOfTransactionByType;
import greenbuildings.enterprise.entities.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends AbstractBaseRepository<TransactionEntity> {
    List<TransactionEntity> findAllByBuilding_IdAndEnterprise_Id(UUID buildingId, UUID enterpriseId);

    Page<TransactionEntity> findAllByBuilding_IdAndEnterprise_Id(UUID buildingId, UUID enterpriseId, Pageable pageable);

    @Query("SELECT t FROM TransactionEntity t WHERE LOWER(t.enterprise.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY t.createdDate DESC")
    Page<TransactionEntity> findAllOrderByCreatedDateDesc(Pageable pageable, String name);
    
    @Query("""
        select new greenbuildings.enterprise.dtos.NumberOfTransactionByType(
            t.transactionType,
                 count(t.id)
        )
        from TransactionEntity t
        group by t.transactionType
    """)
    List<NumberOfTransactionByType> countByTransactionType();
}
