package greenbuildings.idp.mapper.decoratos;

import greenbuildings.idp.dto.EnterpriseUserDTO;
import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.entity.UserPermissionEntity;
import greenbuildings.idp.mapper.EnterpriseUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public abstract class EnterpriseUserMapperDecorator implements EnterpriseUserMapper {
    
    @Autowired
    @Qualifier("delegate")
    private EnterpriseUserMapper delegate;
    
    @Override
    public EnterpriseUserDTO userEntityToEnterpriseUserDTO(UserEntity user) {
        EnterpriseUserDTO dto = delegate.userEntityToEnterpriseUserDTO(user);
        UserPermissionEntity userPermissionEntity = user.getAuthorities().stream().findFirst().orElse(null);
        return new EnterpriseUserDTO(dto.id(), dto.name(), dto.email(), userPermissionEntity.getRole(), dto.emailVerified());
    }
}
