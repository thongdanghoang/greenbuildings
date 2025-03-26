package commons.springfw.impl.securities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import greenbuildings.commons.api.dto.auth.BuildingPermissionDTO;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserContextData implements UserDetails {
    private final String username;
    private final UUID id;
    private final UUID enterpriseId;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private final transient List<BuildingPermissionDTO> permissions;
}
