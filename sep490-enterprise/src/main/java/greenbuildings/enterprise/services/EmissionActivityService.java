package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionActivityCriteria;
import greenbuildings.enterprise.dtos.emission_activities.ActivityCriteria;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.entities.EmissionFactorEntity;
import greenbuildings.enterprise.entities.EmissionSourceEntity;
import greenbuildings.enterprise.models.ActivityRecordDateRange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface EmissionActivityService {
    Page<EmissionActivityEntity> search(SearchCriteriaDTO<EmissionActivityCriteria> searchCriteria);
    
    Page<EmissionActivityEntity> search(Pageable pageable, UUID enterpriseId, ActivityCriteria criteria);
    
    List<BuildingEntity> getBuildingsByEnterpriseId();
    
    List<EmissionFactorEntity> getEmissionFactorsByEnterpriseId(UUID enterpriseId);
    
    EmissionActivityEntity addOrUpdate(EmissionActivityEntity entity);
    
    void deleteActivities(Set<UUID> ids);
    
    EmissionActivityEntity getEmissionActivityDetails(UUID id);

    List<EmissionActivityEntity> getAllActivitiesByBuildingId(UUID id);
    
    List<ActivityRecordDateRange> findRecordedDateRangesById(UUID id, UUID excludeRecordId, UUID assetId);
    
    Map<BuildingEntity, BigDecimal> getTopBuildingsWithHighestEmissions(List<EmissionActivityEntity> activities, int limit);
    
    Map<EmissionSourceEntity, BigDecimal> getTopEmissionSourcesWithHighestEmissions(List<EmissionActivityEntity> activities, int limit);
    
    BigDecimal calculateTotalEmissions(List<EmissionActivityEntity> activities);
    
    List<EmissionActivityEntity> calculationActivitiesTotalGhg(UUID enterpriseId);
}
