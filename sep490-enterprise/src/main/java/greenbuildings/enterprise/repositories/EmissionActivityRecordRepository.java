package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmissionActivityRecordRepository extends JpaRepository<EmissionActivityRecordEntity, UUID>, JpaSpecificationExecutor<EmissionActivityRecordEntity> {
    Page<EmissionActivityRecordEntity> findAllByEmissionActivityEntityId(UUID emissionActivityId, Pageable pageable);
}
