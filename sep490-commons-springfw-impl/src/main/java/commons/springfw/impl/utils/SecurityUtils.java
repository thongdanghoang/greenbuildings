package commons.springfw.impl.utils;

import commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.api.security.UserRole;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public final class SecurityUtils {
    
    private SecurityUtils() {
        // Utility class. No instantiation allowed.
    }
    
    public static Optional<String> getCurrentUserEmail() {
        return getUserContextData().map(UserContextData::getEmail);
    }
    
    public static Optional<UUID> getCurrentUserEnterpriseId() {
        return getUserContextData()
                .map(UserContextData::getPermissions)
                .filter(p -> p.keySet().stream().anyMatch(k -> k == UserRole.ENTERPRISE_OWNER))
                .map(p -> p.get(UserRole.ENTERPRISE_OWNER))
                .flatMap(uuid -> uuid);
    }
    
    public static Optional<UUID> getCurrentUserTenantId() {
        return getUserContextData()
                .map(UserContextData::getPermissions)
                .map(p -> p.get(UserRole.TENANT))
                .flatMap(uuid -> uuid);
    }
    
    public static Optional<UserContextData> getUserContextData() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        var principal = authentication.getPrincipal();
        if (principal instanceof UserContextData currentUser) {
            return Optional.of(currentUser);
        }
        return Optional.empty();
    }
    
}
