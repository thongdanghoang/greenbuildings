package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.dtos.EmissionActivityCriteria;
import greenbuildings.enterprise.entities.ActivityTypeEntity;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.repositories.ActivityTypeRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.services.EmissionActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Throwable.class)
@Slf4j
@RequiredArgsConstructor
public class EmissionActivityServiceImpl implements EmissionActivityService {

    private final EmissionActivityRepository emissionActivityRepository;
    private final ActivityTypeRepository typeRepository;


    @Override
    public Page<EmissionActivityEntity> search(SearchCriteriaDTO<EmissionActivityCriteria> searchCriteria) {
        return emissionActivityRepository
                .findAllByBuildingGroupIdAndNameContainingIgnoreCase(
                        searchCriteria.criteria().buildingId(),
                        Optional.ofNullable(searchCriteria.criteria().name()).orElse(""),
                        CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort()));
    }
    
    @Override
    public EmissionActivityEntity addOrUpdate(EmissionActivityEntity entity) {
        mapActivityType(entity);
        if (entity.getId() == null) {
            entity = emissionActivityRepository.save(entity);
            return emissionActivityRepository.findDetailsById(entity.getId()).orElseThrow();
        }
        EmissionActivityEntity existing = emissionActivityRepository.findById(entity.getId()).orElseThrow();
        updateActivity(entity, existing);
        emissionActivityRepository.save(existing);

        return emissionActivityRepository.findDetailsById(entity.getId()).orElseThrow();
    }
    
    private void mapActivityType(EmissionActivityEntity entity) {
        if (entity.getType() == null) {
            return;
        }
        if (entity.getType().getId() == null) {
            ActivityTypeEntity type = typeRepository.save(entity.getType());
            entity.setType(type);
        } else {
            entity.setType(typeRepository.findById(entity.getType().getId()).orElseThrow());
        }
    }
    
    private void updateActivity(EmissionActivityEntity entity, EmissionActivityEntity existing) {
        existing.setName(entity.getName());
        existing.setType(entity.getType());
        existing.setCategory(entity.getCategory());
        existing.setDescription(entity.getDescription());
    }
    
    @Override
    public void deleteActivities(Set<UUID> ids) {
        if (!emissionActivityRepository.existsAllById(ids)) {
            throw new BusinessException("ids", "http.error.status.404", Collections.emptyList());
        }
        emissionActivityRepository.deleteAllById(ids);
    }
    
    @Override
    public EmissionActivityEntity getEmissionActivityDetails(UUID id) {
        return emissionActivityRepository.findDetailsById(id).orElseThrow(() -> new BusinessException("id", "http.error.status.404", Collections.emptyList()));
    }

    @Override
    public List<EmissionActivityEntity> getAllActivitiesByBuildingId(UUID id) {
        return emissionActivityRepository.findByBuildingGroupId(id);
    }
}
