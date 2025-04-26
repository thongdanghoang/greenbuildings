package greenbuildings.idp.validators;

import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.security.UserScope;
import greenbuildings.idp.entity.BuildingPermissionEntity;
import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class UserValidator {
    
    private final UserRepository userRepository;
    
    public void validateEnterpriseOwnerManageEmployees(UserEntity employee) {
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
        // validators need query should be last
        if (Objects.isNull(employee.getId())
            && userRepository.existsByEmail(employee.getEmail())) {
            throw new BusinessException("email", "business.email.exist");
        }
    }
    
}

