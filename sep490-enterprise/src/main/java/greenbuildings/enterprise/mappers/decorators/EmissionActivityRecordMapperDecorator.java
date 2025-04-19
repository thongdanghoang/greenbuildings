package greenbuildings.enterprise.mappers.decorators;

import greenbuildings.enterprise.dtos.NewEmissionActivityRecordDTO;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.entities.GroupItemEntity;
import greenbuildings.enterprise.mappers.EmissionActivityRecordMapper;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.repositories.GroupItemRepository;
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
    
    @Autowired
    private GroupItemRepository groupItemRepo;
    

    @Override
    public EmissionActivityRecordEntity newToEntity(NewEmissionActivityRecordDTO emissionActivityRecordDTO) {
        EmissionActivityRecordEntity entity = delegate.newToEntity(emissionActivityRecordDTO);
        GroupItemEntity item = groupItemRepo.findById(emissionActivityRecordDTO.groupItemId()).orElseThrow();
        entity.setGroupItem(item);
        return entity;
    }
}
