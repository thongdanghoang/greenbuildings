package greenbuildings.enterprise.repositories;

import greenbuildings.commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.models.ActivityRecordDateRange;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EmissionActivityRepository extends AbstractBaseRepository<EmissionActivityEntity>,
        JpaSpecificationExecutor<EmissionActivityEntity> {
    
    @EntityGraph(value = EmissionActivityEntity.SEARCH_PAGE_GRAPH)
    @Query("FROM EmissionActivityEntity ea WHERE ea.id IN :ids")
    List<EmissionActivityEntity> findWithRecords(Set<UUID> ids, Sort sort);
    
    @Query("""
            SELECT new greenbuildings.enterprise.models.ActivityRecordDateRange(r.startDate, r.endDate)
            FROM EmissionActivityEntity ea
            JOIN ea.records r
            LEFT JOIN r.asset a
            WHERE ea.id = :activityId
            AND ((:assetId IS NOT NULL AND a.id = :assetId) OR (:assetId IS NULL AND a.id IS NULL))
            AND (:excludeRecordId IS NULL OR r.id <> :excludeRecordId)
            """)
    List<ActivityRecordDateRange> findRecordedDateRangesById(UUID activityId, UUID excludeRecordId, UUID assetId);
    
    @Query("""
            SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
            FROM EmissionActivityEntity ea
            JOIN ea.records r
            LEFT JOIN r.asset a
            WHERE ea.id = :activityId
            AND ((:assetId IS NOT NULL AND a.id = :assetId) OR (:assetId IS NULL AND a.id IS NULL))
            AND (:excludeRecordId IS NULL OR r.id <> :excludeRecordId)
            AND r.startDate <= :endDate
            AND r.endDate >= :startDate
            """)
    boolean existsRecordDateRangesNotOverlap(UUID activityId, UUID excludeRecordId, UUID assetId, LocalDate startDate, LocalDate endDate);
    
    List<EmissionActivityEntity> findByBuildingGroupId(UUID id);
    
    List<EmissionActivityEntity> findByBuildingIdAndBuildingGroupIsNull(UUID id);
    
    List<EmissionActivityEntity> findByBuildingGroupIdIn(Set<UUID> ids);
    
    @EntityGraph(attributePaths = {"emissionFactorEntity", "type"})
    Page<EmissionActivityEntity> findAllByBuildingGroupIdAndNameContainingIgnoreCase(UUID buildingId, String activityName, Pageable pageable);
    
    @EntityGraph(attributePaths = {"emissionFactorEntity", "type"})
    Page<EmissionActivityEntity> findAllByBuildingIdAndNameContainingIgnoreCaseAndBuildingGroupIsNull(UUID buildingId, String activityName, Pageable pageable);
    
    Integer countAllByIdIn(Set<UUID> ids);
    
    default boolean existsAllById(Set<UUID> ids) {
        return countAllByIdIn(ids).equals(ids.size());
    }
    
    @EntityGraph(value = EmissionActivityEntity.DETAILS_GRAPH)
    Optional<EmissionActivityEntity> findDetailsById(UUID id);
    
    List<EmissionActivityEntity> findAllByTypeIdIn(Set<UUID> typeIds);
    
    long countByBuildingIdAndBuildingGroupIsNull(UUID buildingId);
    
    long countByBuildingId(UUID buildingId);
    
    @Query("""
                SELECT ea
                FROM EmissionActivityEntity ea
                WHERE ea.building.enterprise.id = :enterpriseId
            """)
    @EntityGraph(value = EmissionActivityEntity.RECORDS_BUILDING_GROUPS_DETAIL)
    List<EmissionActivityEntity> findByEnterpriseId(UUID enterpriseId);
    
    @Query("""
                SELECT ea
                FROM EmissionActivityEntity ea
                WHERE ea.building.enterprise.id = :enterpriseId
                AND ea.building.id IN :buildings
            """)
    @EntityGraph(value = EmissionActivityEntity.RECORDS_BUILDING_GROUPS_DETAIL)
    List<EmissionActivityEntity> findByEnterpriseAndBuildings(UUID enterpriseId, Set<UUID> buildings);
}
