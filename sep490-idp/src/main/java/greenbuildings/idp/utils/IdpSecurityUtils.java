package greenbuildings.idp.utils;

import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.entity.UserPermissionEntity;
import greenbuildings.idp.security.PasskeyAuthenticationToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public final class IdpSecurityUtils {
    
    private static final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    
    private static final String ROLE_PREFIX = "ROLE_";
    
    private IdpSecurityUtils() {
        // Utility class. No instantiation allowed.
    }
    
    public static List<GrantedAuthority> getAuthoritiesFromUserRole(UserEntity user) {
        return user.getAuthorities()
                   .stream()
                   .map(UserPermissionEntity::getRole)
                   .distinct()
                   .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX.concat(role.getCode())))
                   .collect(Collectors.toList());
    }
    
    public static void storeAuthenticationToContext(@NotNull PasskeyAuthenticationToken authenticationToken,
                                                    @NotNull HttpServletRequest request,
                                                    @NotNull HttpServletResponse response) {
        try {
            SecurityContext newContext = SecurityContextHolder.createEmptyContext();
            newContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(newContext);
            securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw new SecurityException("Failed to store authentication context", e);
        }
    }
}
