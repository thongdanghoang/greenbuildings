package greenbuildings.enterprise.mappers.decorators;

import greenbuildings.enterprise.mappers.EmissionActivityRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;



@Component
public abstract class EmissionActivityRecordMapperDecorator implements EmissionActivityRecordMapper {
    
    @Autowired
    @Qualifier("delegate")
    private EmissionActivityRecordMapper delegate;
}
