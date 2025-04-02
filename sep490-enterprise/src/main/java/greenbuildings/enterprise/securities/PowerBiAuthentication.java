package greenbuildings.enterprise.securities;

import greenbuildings.commons.api.events.PowerBiAccessTokenAuthResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class PowerBiAuthentication implements Authentication {
    
    private final PowerBiAccessTokenAuthResult contextData;
    
    @Override
    public PowerBiAccessTokenAuthResult getPrincipal() {
        return contextData;
    }
    
    @Override
    public Object getDetails() {
        return null;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
    
    @Override
    public Object getCredentials() {
        return null;
    }
    
    @Override
    public boolean isAuthenticated() {
        return true;
    }
    
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }
    
    @Override
    public String getName() {
        return "";
    }
}
