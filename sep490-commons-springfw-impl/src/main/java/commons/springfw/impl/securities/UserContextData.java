package commons.springfw.impl.securities;

import greenbuildings.commons.api.security.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserContextData implements UserDetails {
    private final String email;
    private final UUID id;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private final Map<UserRole, Optional<UUID>> permissions; // UserRole:ReferenceId
    
    @Deprecated
    @Override
    public String getUsername() {
        return email;
    }
    
    public UUID getEnterpriseId() {
        return permissions.get(UserRole.ENTERPRISE_OWNER).orElseThrow();
    }
    
    public UUID getTenantId() {
        return permissions.get(UserRole.TENANT).orElseThrow();
    }
}
