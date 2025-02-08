package commons.springfw.impl.securities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sep490.common.api.dto.auth.BuildingPermissionDTO;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserContextData implements UserDetails {
    private final String username;
    private final UUID enterpriseId;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private final List<BuildingPermissionDTO> permissions;
}
