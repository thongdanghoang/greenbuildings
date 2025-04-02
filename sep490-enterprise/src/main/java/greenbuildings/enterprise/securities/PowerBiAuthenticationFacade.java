package greenbuildings.enterprise.securities;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class PowerBiAuthenticationFacade implements IAuthenticationFacade {
    
    @Override
    public PowerBiAuthentication getAuthentication() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof PowerBiAuthentication powerBiAuthentication) {
            return powerBiAuthentication;
        }
        return null;
    }
}

