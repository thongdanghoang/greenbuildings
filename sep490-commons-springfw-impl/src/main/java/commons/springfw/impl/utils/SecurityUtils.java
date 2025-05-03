package commons.springfw.impl.utils;

import commons.springfw.impl.securities.UserContextData;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class SecurityUtils {
    
    private SecurityUtils() {
        // Utility class. No instantiation allowed.
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
