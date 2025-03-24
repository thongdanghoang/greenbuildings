package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionActivityRecordCriteria;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import org.springframework.data.domain.Page;

public interface EmissionActivityRecordService {
    Page<EmissionActivityRecordEntity> search(SearchCriteriaDTO<EmissionActivityRecordCriteria> searchCriteria);

    EmissionActivityRecordEntity addOrUpdate(EmissionActivityRecordEntity entity);
} 