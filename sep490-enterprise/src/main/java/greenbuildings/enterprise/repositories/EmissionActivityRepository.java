package greenbuildings.enterprise.repositories;

import commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.models.ActivityRecordDateRange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EmissionActivityRepository extends AbstractBaseRepository<EmissionActivityEntity> {
    
    @Query("""
            SELECT new greenbuildings.enterprise.models.ActivityRecordDateRange(r.startDate, r.endDate)
            FROM EmissionActivityEntity ea
            JOIN ea.records r
            WHERE ea.id = :activityId
            AND (:excludeRecordId IS NULL OR r.id <> :excludeRecordId)
            """)
    List<ActivityRecordDateRange> findRecordedDateRangesById(UUID activityId, UUID excludeRecordId);
    
    List<EmissionActivityEntity> findByBuildingGroupId(UUID id);
    
    List<EmissionActivityEntity> findByBuildingIdAndBuildingGroupIsNull(UUID id);
    
    List<EmissionActivityEntity> findByBuildingGroupIdIn(Set<UUID> ids);

    Page<EmissionActivityEntity> findAllByBuildingGroupIdAndNameContainingIgnoreCase(UUID buildingId, String activityName,Pageable pageable);
    
    Page<EmissionActivityEntity> findAllByBuildingIdAndNameContainingIgnoreCaseAndBuildingGroupIsNull(UUID buildingId, String activityName,Pageable pageable);
    
    Integer countAllByIdIn(Set<UUID> ids);
    
    default boolean existsAllById(Set<UUID> ids) {
        return countAllByIdIn(ids).equals(ids.size());
    }

    @EntityGraph(value = EmissionActivityEntity.DETAILS_GRAPH)
    Optional<EmissionActivityEntity> findDetailsById(UUID id);
    
    List<EmissionActivityEntity> findAllByIdIn(List<UUID> ids);

    List<EmissionActivityEntity> findAllByTypeIdIn(Set<UUID> typeIds);
    
    long countByBuildingIdAndBuildingGroupIsNull(UUID buildingId);
}
