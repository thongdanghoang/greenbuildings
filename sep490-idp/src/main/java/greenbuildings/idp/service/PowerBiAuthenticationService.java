package greenbuildings.idp.service;

import greenbuildings.idp.entity.PowerBiAuthority;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PowerBiAuthenticationService {
    
    void authenticate(Message<String> apiKeyMsg);
    
    void delete(UUID id);
    
    PowerBiAuthority createOrUpdate(PowerBiAuthority powerBiAuthority);
    
    PowerBiAuthority regenerateApiKey(PowerBiAuthority powerBiAuthority);
    
    List<PowerBiAuthority> findAllByUser(UUID userId);
    
    Optional<PowerBiAuthority> findById(UUID id);
}
