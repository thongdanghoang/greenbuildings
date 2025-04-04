package greenbuildings.idp.mapper;

import greenbuildings.commons.api.dto.auth.BuildingPermissionDTO;
import greenbuildings.idp.dto.EnterpriseUserDTO;
import greenbuildings.idp.dto.EnterpriseUserDetailsDTO;
import greenbuildings.idp.dto.UserByBuildingDTO;
import greenbuildings.idp.entity.BuildingPermissionEntity;
import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.mapper.decoratos.EnterpriseUserMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@DecoratedWith(EnterpriseUserMapperDecorator.class)
public interface EnterpriseUserMapper {
    
    @Mapping(target = "scope", source = "enterprise.scope")
    @Mapping(target = "name", source = ".", qualifiedByName = "toFullName")
    EnterpriseUserDTO userEntityToEnterpriseUserDTO(UserEntity user);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "name", source = "user", qualifiedByName = "toFullName")
    UserByBuildingDTO userEntityToUserByBuildingDTO(UserEntity user, UUID buildingId);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "phoneVerified", ignore = true)
    @Mapping(target = "enterprise.scope", source = "scope")
    UserEntity createNewEnterpriseUser(EnterpriseUserDetailsDTO dto);
    
    @Mapping(target = "scope", source = "enterprise.scope")
    EnterpriseUserDetailsDTO userEntityToEnterpriseUserDetailDTO(UserEntity user);
    
    @Mapping(target = "scope", source = "enterprise.scope")
    @Mapping(target = "buildingPermissions", ignore = true)
    EnterpriseUserDetailsDTO userEntityToBasicEnterpriseUserDetailDTO(UserEntity user);
    
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "phoneVerified", ignore = true)
    @Mapping(target = "enterprise.scope", source = "scope")
    void updateEnterpriseUser(@MappingTarget UserEntity user, EnterpriseUserDetailsDTO dto);
    
    @Mapping(target = "buildingId", source = "building")
    BuildingPermissionDTO toBuildingPermissionDTO(BuildingPermissionEntity entity);
    
    @Mapping(target = "user", ignore = true) // set in decorator or controller
    @InheritInverseConfiguration
    BuildingPermissionEntity toBuildingPermissionEntity(BuildingPermissionDTO dto);
    
    Set<BuildingPermissionEntity> toBuildingPermissionEntities(List<BuildingPermissionDTO> dtos);
    
    @Named("toFullName")
    default String translateToFullName(UserEntity user) {
        return user.getFirstName() + " " + user.getLastName();
    }
    
}
