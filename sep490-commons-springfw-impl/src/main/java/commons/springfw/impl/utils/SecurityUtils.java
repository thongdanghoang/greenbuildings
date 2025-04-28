package commons.springfw.impl.utils;

import commons.springfw.impl.securities.UserContextData;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import greenbuildings.commons.api.dto.auth.BuildingPermissionDTO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class SecurityUtils {
    
    private SecurityUtils() {
        // Utility class. No instantiation allowed.
    }
    
    public static Optional<String> getCurrentUserEmail() {
        return getUserContextData().map(UserContextData::getUsername);
    }

    public static Optional<UUID> getCurrentUserEnterpriseId() {
        return getUserContextData().map(UserContextData::getEnterpriseId);
    }
    
    public static List<BuildingPermissionDTO> getPermissions() {
        Optional<UserContextData> currentUser = getUserContextData();
        return currentUser
                .map(UserContextData::getPermissions)
                .orElse(Collections.emptyList());
    }
    
    public static Optional<UserContextData> getUserContextData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserContextData currentUser) {
            return Optional.of(currentUser);
        }
        return Optional.empty();
    }
    
}
