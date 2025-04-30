package commons.springfw.impl.securities;

import greenbuildings.commons.api.security.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationConverter
        implements Converter<Jwt, JwtAuthenticationTokenDecorator> {
    
    @Override
    public JwtAuthenticationTokenDecorator convert(Jwt source) {
        var userId = UUID.fromString(Objects.requireNonNull(source.getClaims().get("sub")).toString());
        var email = Objects.requireNonNull(source.getClaims().get("email")).toString();
        var authoritiesClaim = Objects.requireNonNull(source.getClaims().get("authorities"));
        Set<SimpleGrantedAuthority> authorities = Collections.emptySet();
        if (authoritiesClaim instanceof List<?> authoritiesLists) {
            authorities = authoritiesLists
                    .stream()
                    .filter(String.class::isInstance)
                    .map(authority -> new SimpleGrantedAuthority((String) authority))
                    .collect(Collectors.toUnmodifiableSet());
        }
        
        var permissionsClaim = Optional.ofNullable(source.getClaims().get("permissions")).orElse(Collections.emptyList());
        Map<UserRole, Optional<UUID>> permissions = Collections.emptyMap();
        if (permissionsClaim instanceof List<?> permissionsList) {
            permissions = permissionsList
                    .stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast) // UUID:String
                    .collect(Collectors.toMap(
                            permission -> UserRole.valueOf(permission.split(":")[0]), // Extract UserRole
                            permission -> isReferenceId(permission)
                                          ? Optional.of(UUID.fromString(permission.split(":")[1]))
                                          : Optional.empty(), // Extract or set empty Optional
                            (existing, replacement) -> existing // Keep the first value in case of conflict
                                             ));
        }
        
        return new JwtAuthenticationTokenDecorator(
                source,
                new UserContextData(
                        email,
                        userId,
                        StringUtils.EMPTY,
                        List.copyOf(authorities),
                        Map.copyOf(permissions)
                )
        );
    }
    
    private boolean isReferenceId(String permission) {
        return permission.matches("^[^:]+:[^:]+$");
    }
}
