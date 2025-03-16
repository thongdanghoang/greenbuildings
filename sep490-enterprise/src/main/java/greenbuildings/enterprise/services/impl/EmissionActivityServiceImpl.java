package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionActivityCriteria;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.services.EmissionActivityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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
}
