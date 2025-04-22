package greenbuildings.enterprise.rest;

import commons.springfw.impl.controller.AbstractRestController;
import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.dtos.TenantDTO;
import greenbuildings.enterprise.mappers.TenantMapper;
import greenbuildings.enterprise.services.TenantService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tenants")
@RequiredArgsConstructor
@RolesAllowed({
        UserRole.RoleNameConstant.ENTERPRISE_OWNER
})
public class TenantController extends AbstractRestController {
    
    private final TenantService tenantService;
    private final TenantMapper tenantMapper;
    
    @GetMapping("/{id}")
    public ResponseEntity<TenantDTO> findById(@PathVariable UUID id) {
        return tenantService.findById(id)
                .map(tenantMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<TenantDTO>> findAll() {
        return ResponseEntity.ok(tenantService.findAll().stream()
                .map(tenantMapper::toDTO)
                .toList());
    }
    
    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<TenantDTO>> search(@RequestBody SearchCriteriaDTO<Void> searchCriteria) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = tenantService.findAll(pageable);
        return ResponseEntity.ok(CommonMapper.toSearchResultDTO(searchResults, tenantMapper::toDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TenantDTO> update(@PathVariable UUID id, @RequestBody TenantDTO dto) {
        return ResponseEntity.ok(tenantMapper.toDTO(tenantService.update(id, dto)));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        tenantService.delete(id);
        return ResponseEntity.ok().build();
    }
} 