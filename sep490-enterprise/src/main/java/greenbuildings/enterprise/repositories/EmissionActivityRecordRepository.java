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
public interface EmissionActivityRecordRepository extends JpaRepository<EmissionActivityRecordEntity, UUID>, JpaSpecificationExecutor<EmissionActivityRecordEntity> {
    Page<EmissionActivityRecordEntity> findAllByEmissionActivityEntityId(UUID emissionActivityId, Pageable pageable);
    
    List<EmissionActivityRecordEntity> findAllByEmissionActivityEntityId(UUID emissionActivityId);
    
    Integer countAllByIdIn(Set<UUID> ids);
    
    default boolean existsAllById(Set<UUID> ids) {
        return countAllByIdIn(ids).equals(ids.size());
    }
    
    List<EmissionActivityRecordEntity> findAllByIdIn(Set<UUID> ids);

    @Query("""
            SELECT e
            FROM EmissionActivityRecordEntity e
            WHERE e.emissionActivityEntity.id = :emissionActivityId
            AND ((e.startDate BETWEEN :startDate AND :endDate)
                OR (e.endDate BETWEEN :startDate AND :endDate))
    """)
    List<EmissionActivityRecordEntity> findAllByEmissionActivityEntityIdAndDateBetween(UUID emissionActivityId, LocalDate startDate, LocalDate endDate);
}
