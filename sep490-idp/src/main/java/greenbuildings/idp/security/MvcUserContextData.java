package greenbuildings.idp.security;

import commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.idp.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class MvcUserContextData extends UserContextData {
    private final transient UserEntity userEntity;
    
    public MvcUserContextData(@NotNull UserEntity userEntity,
                              List<GrantedAuthority> authorities,
                              Map<UserRole, UUID> permissions) {
        super(userEntity.getEmail(),
              userEntity.getId(),
              userEntity.getPassword(),
              List.copyOf(authorities),
              Map.copyOf(permissions));
        this.userEntity = userEntity;
    }
}
