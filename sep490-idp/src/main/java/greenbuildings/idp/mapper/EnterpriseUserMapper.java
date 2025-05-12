package greenbuildings.idp.mapper;

import greenbuildings.idp.dto.EnterpriseUserDTO;
import greenbuildings.idp.dto.EnterpriseUserDetailsDTO;
import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.mapper.decoratos.EnterpriseUserMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@DecoratedWith(EnterpriseUserMapperDecorator.class)
public interface EnterpriseUserMapper {
    
    @Mapping(target = "name", source = ".", qualifiedByName = "toFullName")
    EnterpriseUserDTO userEntityToEnterpriseUserDTO(UserEntity user);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "phone", ignore = true)
    UserEntity createNewEnterpriseUser(EnterpriseUserDetailsDTO dto);
    
    EnterpriseUserDetailsDTO userEntityToEnterpriseUserDetailDTO(UserEntity user);
    
    EnterpriseUserDetailsDTO userEntityToBasicEnterpriseUserDetailDTO(UserEntity user);
    
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "phone", ignore = true)
    void updateEnterpriseUser(@MappingTarget UserEntity user, EnterpriseUserDetailsDTO dto);
    
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "powerBiApiKeys", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "locale", ignore = true)
    void updateSelfUser(@MappingTarget UserEntity user, EnterpriseUserDetailsDTO dto);
    
    @Named("toFullName")
    default String translateToFullName(UserEntity user) {
        return user.getFirstName() + " " + user.getLastName();
    }
    
}
