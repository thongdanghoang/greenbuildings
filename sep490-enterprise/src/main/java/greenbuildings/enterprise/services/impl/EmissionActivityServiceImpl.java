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
                .findAllByBuildingId(
                        searchCriteria.criteria().buildingId(),
                        CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort()));
    }
    
    @Override
    public EmissionActivityEntity add(EmissionActivityEntity entity) {
        return emissionActivityRepository.save(entity);
    }
    
    @Override
    public void deleteActivities(Set<UUID> ids) {
        if (!emissionActivityRepository.existsAllById(ids)) {
            throw new BusinessException("ids", "http.error.status.404", Collections.emptyList());
        }
        emissionActivityRepository.deleteAllById(ids);
    }
}
