package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EmissionActivityRecordRepository
        extends JpaRepository<EmissionActivityRecordEntity, UUID>, JpaSpecificationExecutor<EmissionActivityRecordEntity> {
    Page<EmissionActivityRecordEntity> findAllByEmissionActivityId(UUID groupItemId, Pageable pageable);
    
    List<EmissionActivityRecordEntity> findAllByEmissionActivityId(UUID groupItemId);
    
    List<EmissionActivityRecordEntity> findAllByIdIn(Set<UUID> ids);
    
    @Query("""
                SELECT r
                FROM EmissionActivityRecordEntity r
                WHERE r.emissionActivity.id = :activityId
                AND r.startDate <= :endDate
                AND r.endDate >= :startDate
            """)
    List<EmissionActivityRecordEntity> findAllByEmissionActivityIdAndDateRangeExclusive(UUID activityId, LocalDate startDate, LocalDate endDate);
}
