package greenbuildings.idp.rest;

import commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.idp.dto.powerbi.PowerBiAuthorityDTO;
import greenbuildings.idp.mapper.PowerBiMapper;
import greenbuildings.idp.service.PowerBiAuthenticationService;
import greenbuildings.idp.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth/power-bi")
@RequiredArgsConstructor
@RolesAllowed({UserRole.RoleNameConstant.ENTERPRISE_OWNER})
public class PowerBiResource {
    
    private final UserService userService;
    private final PowerBiAuthenticationService service;
    private final PowerBiMapper mapper;
    
    @GetMapping
    public ResponseEntity<List<PowerBiAuthorityDTO>> getMyPowerBiAuthority(@AuthenticationPrincipal UserContextData userContextData) {
        var powerBiAuthorities = service.findAllByUser(userContextData.getId());
        return ResponseEntity.ok(powerBiAuthorities.stream().map(mapper::toPowerBiAuthority).toList());
    }
    
    @Builder
    public record ApiKeyResponse(UUID id, String apiKey) {
    }
    
    @PostMapping
    public ResponseEntity<ApiKeyResponse> generateApiKey(@AuthenticationPrincipal UserContextData userContextData,
                                                         @RequestBody PowerBiAuthorityDTO request) {
        var userEntity = userService.findById(userContextData.getId()).orElseThrow();
        var powerBiAuthority = mapper.generateApiKey(request, userEntity);
        if (Objects.nonNull(request.id())) {
            var powerBiAuthorityEntity = service.findById(request.id()).orElseThrow();
            powerBiAuthority = mapper.updateApiKey(request, powerBiAuthorityEntity, userEntity);
        }
        powerBiAuthority.setEnterpriseId(userContextData.getEnterpriseId());
        var powerBiAuthorityPersisted = service.createOrUpdate(powerBiAuthority);
        var response = ApiKeyResponse.builder()
                                     .apiKey(Objects.nonNull(request.id()) ? null : powerBiAuthorityPersisted.getApiKey())
                                     .id(powerBiAuthorityPersisted.getId())
                                     .build();
        return ResponseEntity.ok(response);
    }
    
    @Builder
    public record RegenerateTokenBody(@NotNull LocalDateTime expirationTime) { }
    
    @PostMapping("/{id}/regenerate")
    public ResponseEntity<ApiKeyResponse> regenerateApiKey(@PathVariable UUID id, @RequestBody RegenerateTokenBody request) {
        var powerBiAuthority = service.findById(id).orElseThrow();
        powerBiAuthority.setExpirationTime(request.expirationTime);
        var powerBiAuthorityPersisted = service.regenerateApiKey(powerBiAuthority);
        var response = ApiKeyResponse.builder()
                                     .apiKey(powerBiAuthorityPersisted.getApiKey())
                                     .id(powerBiAuthorityPersisted.getId())
                                     .build();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PowerBiAuthorityDTO> getAccessToken(@PathVariable UUID id) {
        var result = service
                .findById(id)
                .map(mapper::toPowerBiAuthority)
                .orElseThrow();
        return ResponseEntity.ok(result);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessToken(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}
