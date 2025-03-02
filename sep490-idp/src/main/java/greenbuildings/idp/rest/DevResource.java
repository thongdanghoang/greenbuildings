package greenbuildings.idp.rest;

import greenbuildings.idp.producers.IdPEventProducer;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.idp.dto.EnterpriseUserDTO;

import java.security.Principal;
import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
@RolesAllowed({
        UserRole.RoleNameConstant.SYSTEM_ADMIN
})
public class DevResource {
    
    private final IdPEventProducer idPEventProducer;
    
    @GetMapping("/test")
    public ResponseEntity<String> test(Principal principal) {
        return ResponseEntity.ok(principal.getClass().getName());
    }
    
    @PostMapping("/test")
    public ResponseEntity<String> testPost(Principal principal, @RequestBody EnterpriseUserDTO payload) {
        idPEventProducer.publishTestTokenEvent();
        return ResponseEntity.ok(principal.getClass().getName());
    }
    
    @PostMapping("/technical")
    public ResponseEntity<Void> throwTechnicalException() {
        throw new TechnicalException("Technical exception");
    }
    
    @PostMapping("/business")
    public ResponseEntity<Void> throwBusinessException() {
        throw new BusinessException("field", "i18nKey", Collections.emptyList());
    }
    
    @GetMapping("/secure/{buildingId}")
    public ResponseEntity<String> secure(@PathVariable UUID buildingId) {
        return ResponseEntity.ok(buildingId.toString());
    }
    
}
