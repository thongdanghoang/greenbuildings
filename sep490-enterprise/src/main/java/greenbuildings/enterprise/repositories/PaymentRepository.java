package greenbuildings.enterprise.repositories;

import commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.PaymentEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;


public interface PaymentRepository extends AbstractBaseRepository<PaymentEntity> {
    
    Page<PaymentEntity> findByEnterpriseId(UUID enterpriseId, Pageable pageable);
    
    Optional<PaymentEntity> findByOrderCodeAndEnterpriseId(Long orderCode, UUID enterpriseId);

    @NotNull
    @EntityGraph(attributePaths = {
            "enterprise",
            "creditPackageVersionEntity"
    })
    Optional<PaymentEntity> findById(@NotNull UUID id);

    @Query("SELECT t FROM PaymentEntity t WHERE LOWER(t.enterprise.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY t.createdDate DESC")
    Page<PaymentEntity> findAllOrderByCreatedDateDesc(Pageable pageable, String name);
}
