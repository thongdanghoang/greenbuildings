package commons.springfw.impl.securities;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

@Getter
public class JwtAuthenticationTokenDecorator extends JwtAuthenticationToken {
    
    private final UserContextData principal;
    
    public JwtAuthenticationTokenDecorator(Jwt jwt,
                                           UserContextData principal) {
        
        super(jwt, principal.getAuthorities());
        this.principal = principal;
    }
    
    @Override
    public Object getPrincipal() {
        return principal;
    }
    
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return principal.getAuthorities();
    }
}
