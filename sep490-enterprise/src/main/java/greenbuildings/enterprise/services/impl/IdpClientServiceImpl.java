package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.services.IdpClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class IdpClientServiceImpl implements IdpClientService {
    
    private final WebClient idpWebClient;  // This should be configured in your WebClient configuration
    
    @Override
    public Boolean checkEmailExists(String email) {
        return idpWebClient.get()
                           .uri("/api/enterprise-user/check-email?email={tenantEmail}", email)
                           .retrieve()
                           .bodyToMono(Boolean.class)
                           .block();
    }
    
    
    
}
