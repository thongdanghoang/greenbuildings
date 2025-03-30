package greenbuildings.idp.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.commons.api.security.UserScope;
import greenbuildings.idp.entity.BuildingPermissionEntity;
import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.repository.UserRepository;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class UserValidator {
    
    private final UserRepository userRepository;
    
    public void validateEnterpriseOwnerManageEmployees(UserEntity employee) {
        if(employee.getRole() == UserRole.ENTERPRISE_EMPLOYEE){
            if (employee.getBuildingPermissions().isEmpty()) {
                throw new BusinessException("buildingPermissions", "business.buildingPermissions.notEmpty");
            }
        }

        if (employee.getEnterprise().getScope() == UserScope.BUILDING) {
            if (employee.getBuildingPermissions().stream()
                        .map(BuildingPermissionEntity::getBuilding)
                        .anyMatch(Objects::isNull)) {
                throw new BusinessException("buildingPermissions", "business.buildingPermissions.shouldHave");
            }
            if (employee.getBuildingPermissions().stream()
                        .map(BuildingPermissionEntity::getBuilding)
                        .distinct()
                        .count() != employee.getBuildingPermissions().size()) {
                throw new BusinessException("buildingPermissions", "business.buildingPermissions.shouldHaveUnique");
            }
        }
        if (employee.getEnterprise().getScope() == UserScope.ENTERPRISE && employee.getRole() == UserRole.ENTERPRISE_EMPLOYEE) {
            if (employee.getBuildingPermissions().size() != 1
                || employee.getBuildingPermissions().stream()
                           .map(BuildingPermissionEntity::getBuilding)
                           .anyMatch(Objects::nonNull)) {
                throw new BusinessException("buildingPermissions",
                                            "business.buildingPermissions.idBuildingIsNull");
            }
        }
        // validators need query should be last
        if (Objects.isNull(employee.getId())
            && userRepository.existsByEmail(employee.getEmail())) {
            throw new BusinessException("email", "business.email.exist");
        }
    }
    
}

