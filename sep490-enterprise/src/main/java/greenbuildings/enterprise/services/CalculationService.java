package greenbuildings.enterprise.services;

import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface CalculationService {
    List<EmissionActivityRecordEntity> calculate(@NotNull UUID activityId, Collection<EmissionActivityRecordEntity> content);
    
    EmissionActivityEntity calculate(EmissionActivityEntity activity);
}
