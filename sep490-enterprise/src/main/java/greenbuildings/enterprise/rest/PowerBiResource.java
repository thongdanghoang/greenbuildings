package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.securities.PowerBiAuthenticationFacade;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/power-bi/{apiKey}")
@RequiredArgsConstructor
@Slf4j
public class PowerBiResource {
    
    private final PowerBiAuthenticationFacade authenticationFacade;
    
    @GetMapping(value = "/poc", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@powerBiSecurityCheckerBean.checkIfUserHasPermission(#apiKey)")
    public ResponseEntity<String> poc(@PathVariable @NotBlank String apiKey) throws IOException {
        var contextData = authenticationFacade.getAuthentication().getPrincipal();
        // TODO: use context data to get the buildings according to buildings permissions and enterprise Id
        try (InputStream inputStream = Thread.currentThread()
                                             .getContextClassLoader()
                                             .getResourceAsStream("power-bi-response.json")) {
            
            return ResponseEntity.ok(StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8));
        }
    }
    
}
