package greenbuildings.enterprise.mappers.decorators;

import greenbuildings.enterprise.dtos.NewEmissionActivityRecordDTO;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.mappers.EmissionActivityRecordMapper;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public abstract class EmissionActivityRecordMapperDecorator implements EmissionActivityRecordMapper {

    @Autowired
    @Qualifier("delegate")
    private EmissionActivityRecordMapper delegate;
    
    @Autowired
    private EmissionActivityRepository activityRepository;

    @Override
    public EmissionActivityRecordEntity newToEntity(NewEmissionActivityRecordDTO emissionActivityRecordDTO) {
        EmissionActivityRecordEntity entity = delegate.newToEntity(emissionActivityRecordDTO);
        return entity;
    }
}
