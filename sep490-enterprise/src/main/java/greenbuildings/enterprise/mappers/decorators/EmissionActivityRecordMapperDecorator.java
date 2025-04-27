package greenbuildings.enterprise.mappers.decorators;

import greenbuildings.enterprise.dtos.NewEmissionActivityRecordDTO;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.entities.GroupItemEntity;
import greenbuildings.enterprise.mappers.EmissionActivityRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public abstract class EmissionActivityRecordMapperDecorator implements EmissionActivityRecordMapper {
    
    @Autowired
    @Qualifier("delegate")
    private EmissionActivityRecordMapper delegate;
    
    
    @Override
    public EmissionActivityRecordEntity newToEntity(NewEmissionActivityRecordDTO emissionActivityRecordDTO) {
        var entity = delegate.newToEntity(emissionActivityRecordDTO);
        if (Objects.nonNull(emissionActivityRecordDTO.groupItemId())) {
            var groupItemEntity = new GroupItemEntity();
            groupItemEntity.setId(emissionActivityRecordDTO.groupItemId());
            entity.setGroupItem(groupItemEntity);
        }
        return entity;
    }
}
