package greenbuildings.enterprise.rest;
import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.dtos.EnterpriseDetailDTO;
import greenbuildings.enterprise.mappers.EnterpriseMapper;
import greenbuildings.enterprise.services.EnterpriseService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/enterprise")
@RequiredArgsConstructor
@RolesAllowed({
        UserRole.RoleNameConstant.ENTERPRISE_OWNER
})
public class EnterpriseController {
    
    private final EnterpriseService service;
    private final EnterpriseMapper mapper;
    
    @GetMapping("/profile")
    public ResponseEntity<EnterpriseDetailDTO> getEnterpriseProfile() {
        UUID enterpriseId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
        return ResponseEntity.ok(service.getEnterpriseDetail(enterpriseId));
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateEnterpriseProfile(
            @Valid @RequestBody EnterpriseDetailDTO dto) {
        UUID enterpriseId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
        // Ensure the ID in the DTO matches the authenticated enterprise ID
        if (!dto.id().equals(enterpriseId)) {
            return ResponseEntity.badRequest().build();
        }

        service.updateEnterpriseDetail(dto);
        return ResponseEntity.ok().build();
    }
}
