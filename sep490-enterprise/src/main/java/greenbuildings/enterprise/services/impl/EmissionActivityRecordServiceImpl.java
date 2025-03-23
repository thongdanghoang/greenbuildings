package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionActivityRecordCriteria;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.repositories.EmissionActivityRecordRepository;
import greenbuildings.enterprise.services.EmissionActivityRecordService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
@Transactional(rollbackOn = Throwable.class)
@RequiredArgsConstructor
public class EmissionActivityRecordServiceImpl implements EmissionActivityRecordService {

    private final EmissionActivityRecordRepository recordRepository;

    @Override
    public Page<EmissionActivityRecordEntity> search(SearchCriteriaDTO<EmissionActivityRecordCriteria> searchCriteria) {
        return recordRepository
                .findAllByEmissionActivityEntityId(
                        searchCriteria.criteria().emissionActivityId(),
                        CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort()));
    }
} 