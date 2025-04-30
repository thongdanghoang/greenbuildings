package greenbuildings.idp.mapper.decoratos;

import greenbuildings.idp.mapper.EnterpriseUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public abstract class EnterpriseUserMapperDecorator implements EnterpriseUserMapper {
    
    @Autowired
    @Qualifier("delegate")
    private EnterpriseUserMapper delegate;
    
}
