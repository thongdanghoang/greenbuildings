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
    Page<EmissionActivityRecordEntity> findAllByGroupItemId(UUID groupItemId, Pageable pageable);
    
    List<EmissionActivityRecordEntity> findAllByGroupItemId(UUID groupItemId);
    
    Integer countAllByIdIn(Set<UUID> ids);
    
    default boolean existsAllById(Set<UUID> ids) {
        return countAllByIdIn(ids).equals(ids.size());
    }
    
    List<EmissionActivityRecordEntity> findAllByIdIn(Set<UUID> ids);
    
    @Query("""
                SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END
                FROM EmissionActivityRecordEntity e
                WHERE e.groupItem.id = :groupItemId
                AND (
                    (e.startDate < :endDate AND e.endDate > :startDate)
                )
            """)
    boolean existsByGroupItemIdAndDateOverlap(UUID groupItemId, LocalDate startDate, LocalDate endDate);
    
    
    @Query("""
                SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END
                FROM EmissionActivityRecordEntity e
                WHERE e.id <> :recordId
                AND e.groupItem.id = :groupItemId
                AND (
                    (e.startDate < :endDate AND e.endDate > :startDate)
                )
            """)
    boolean otherExistsByGroupItemIdAndDateOverlap(UUID recordId, UUID groupItemId, LocalDate startDate, LocalDate endDate);
    
    
}
