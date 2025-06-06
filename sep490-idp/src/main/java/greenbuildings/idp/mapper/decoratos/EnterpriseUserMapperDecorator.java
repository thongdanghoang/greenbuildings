package greenbuildings.idp.mapper.decoratos;

import greenbuildings.commons.api.security.UserRole;
import greenbuildings.idp.dto.EnterpriseUserDTO;
import greenbuildings.idp.entity.UserEntity;
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
        var userDTO = delegate.userEntityToEnterpriseUserDTO(user);
        var enterpriseUserDTO = new EnterpriseUserDTO(userDTO.id(), userDTO.name(), userDTO.email(), null, userDTO.emailVerified());
        if (user.getAuthorities().stream().anyMatch(u -> u.getRole() == UserRole.ENTERPRISE_OWNER)) {
            return enterpriseUserDTO.toBuilder().role(UserRole.ENTERPRISE_OWNER).build();
        }
        if (user.getAuthorities().stream().anyMatch(u -> u.getRole() == UserRole.TENANT)) {
            return enterpriseUserDTO.toBuilder().role(UserRole.TENANT).build();
        }
        if (user.getAuthorities().stream().anyMatch(u -> u.getRole() == UserRole.SYSTEM_ADMIN)) {
            return enterpriseUserDTO.toBuilder().role(UserRole.SYSTEM_ADMIN).build();
        }
        if (user.getAuthorities().stream().anyMatch(u -> u.getRole() == UserRole.BASIC_USER)) {
            return enterpriseUserDTO.toBuilder().role(UserRole.BASIC_USER).build();
        }
        return enterpriseUserDTO;
    }
}
