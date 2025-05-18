package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.securities.PowerBiAuthenticationFacade;
import greenbuildings.enterprise.services.ReportService;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/power-bi/{apiKey}")
@RequiredArgsConstructor
@Slf4j
public class PowerBiResource {
    
    private final PowerBiAuthenticationFacade authenticationFacade;
    private final ReportService reportService;
    
    @GetMapping(value = "/report", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@powerBiSecurityCheckerBean.checkIfUserHasPermission(#apiKey)")
    public ResponseEntity<?> report(@PathVariable @NotBlank String apiKey) throws IOException {
        var contextData = authenticationFacade.getAuthentication().getPrincipal();
        return ResponseEntity.ok(reportService.generateReport(contextData));
    }
    
}
