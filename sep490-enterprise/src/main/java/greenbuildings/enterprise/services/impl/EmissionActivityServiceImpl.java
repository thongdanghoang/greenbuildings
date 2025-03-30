package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.dtos.EmissionActivityCriteria;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.services.EmissionActivityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(rollbackOn = Throwable.class)
@Slf4j
@RequiredArgsConstructor
public class EmissionActivityServiceImpl implements EmissionActivityService {

    private final EmissionActivityRepository emissionActivityRepository;


    @Override
    public Page<EmissionActivityEntity> search(SearchCriteriaDTO<EmissionActivityCriteria> searchCriteria) {
        return emissionActivityRepository
                .findAllByBuildingIdAndNameContainingIgnoreCase(
                        searchCriteria.criteria().buildingId(),
                        Optional.ofNullable(searchCriteria.criteria().name()).orElse(""),
                        CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort()));
    }
    
    @Override
    public EmissionActivityEntity addOrUpdate(EmissionActivityEntity entity) {
        if (entity.getId() == null) {
            entity = emissionActivityRepository.save(entity);
            return emissionActivityRepository.findDetailsById(entity.getId()).orElseThrow();
        }
        EmissionActivityEntity existing = emissionActivityRepository.findById(entity.getId()).orElseThrow();
        updateActivity(entity, existing);
        emissionActivityRepository.save(existing);

        return emissionActivityRepository.findDetailsById(entity.getId()).orElseThrow();
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
}
