package greenbuildings.idp.mapper.decoratos;

import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.commons.api.dto.auth.BuildingPermissionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import greenbuildings.idp.dto.EnterpriseUserDetailsDTO;
import greenbuildings.idp.entity.BuildingPermissionEntity;
import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.mapper.EnterpriseUserMapper;
import greenbuildings.idp.repository.BuildingPermissionRepository;
import greenbuildings.idp.repository.UserRepository;

import java.util.stream.Collectors;

@Component
public abstract class EnterpriseUserMapperDecorator implements EnterpriseUserMapper {
    
    @Autowired
    @Qualifier("delegate")
    private EnterpriseUserMapper delegate;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BuildingPermissionRepository buildingPermissionRepository;

    @Override
    public UserEntity createNewEnterpriseUser(EnterpriseUserDetailsDTO dto) {
        UserEntity user = delegate.createNewEnterpriseUser(dto);
        user.getEnterprise().setEnterprise(SecurityUtils.getCurrentUserEnterpriseId().orElseThrow());
        return user;
    }
    
    @Override
    public void updateEnterpriseUser(UserEntity user, EnterpriseUserDetailsDTO dto) {
        delegate.updateEnterpriseUser(user, dto);
        var buildings = dto
                .buildingPermissions().stream()
                .map(BuildingPermissionDTO::buildingId)
                .collect(Collectors.toSet());
        var buildingPermissions = buildingPermissionRepository
                .findAllByUserIdAndBuildings(user, buildings)
                .stream()
                .collect(Collectors.toMap(BuildingPermissionEntity::getBuilding, BuildingPermissionEntity::getId));
        user.getBuildingPermissions().stream()
            .filter(bp -> buildingPermissions.containsKey(bp.getBuilding()))
            .forEach(bp -> {
                bp.setId(buildingPermissions.get(bp.getBuilding()));
            });
    }
    
    private UserEntity getCurrentUser() {
        return SecurityUtils
                .getCurrentUserEmail()
                .flatMap(email -> userRepository.findByEmail(email))
                .orElseThrow();
    }
}
