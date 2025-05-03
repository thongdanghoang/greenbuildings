package greenbuildings.enterprise.rest;

import commons.springfw.impl.controller.AbstractRestController;
import commons.springfw.impl.mappers.CommonMapper;
import commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.dtos.SearchTenantCriteria;
import greenbuildings.enterprise.dtos.TenantDTO;
import greenbuildings.enterprise.dtos.TenantDetailDTO;
import greenbuildings.enterprise.dtos.TenantTableView;
import greenbuildings.enterprise.mappers.TenantMapper;
import greenbuildings.enterprise.services.TenantService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tenants")
@RequiredArgsConstructor
@RolesAllowed({
        UserRole.RoleNameConstant.ENTERPRISE_OWNER, UserRole.RoleNameConstant.TENANT
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
    
    @GetMapping("/{id}/detail")
    public ResponseEntity<TenantDetailDTO> getTenantDetail(@PathVariable UUID id) {
        return tenantService.getTenantDetail(id)
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/{id}/detail")
    public ResponseEntity<TenantDetailDTO> updateTenantDetail(@PathVariable UUID id, @RequestBody TenantDetailDTO detailDTO) {
        return ResponseEntity.ok(tenantService.updateTenantDetail(id, detailDTO));
    }
    
    @GetMapping
    public ResponseEntity<List<TenantDTO>> findAll() {
        return ResponseEntity.ok(tenantService.findAll().stream()
                                              .map(tenantMapper::toDTO)
                                              .toList());
    }
    
    @PostMapping("/search")
    @RolesAllowed({UserRole.RoleNameConstant.ENTERPRISE_OWNER})
    public ResponseEntity<SearchResultDTO<TenantTableView>> search(
            @RequestBody SearchCriteriaDTO<SearchTenantCriteria> searchCriteria,
            @AuthenticationPrincipal UserContextData userContextData) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var enterpriseID = userContextData.getEnterpriseId().orElseThrow();
        var searchResults = tenantService.search(enterpriseID, searchCriteria, pageable);
        return ResponseEntity.ok(SearchResultDTO.of(searchResults.getContent(), searchResults.getTotalElements()));
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
    
    @PostMapping("/admin-search")
    @RolesAllowed({UserRole.RoleNameConstant.SYSTEM_ADMIN})
    public ResponseEntity<SearchResultDTO<TenantTableView>> searchForAdmin(
            @RequestBody SearchCriteriaDTO<SearchTenantCriteria> searchCriteria) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = tenantService.search(searchCriteria.criteria(), pageable);
        return ResponseEntity.ok(SearchResultDTO.of(searchResults.getContent(), searchResults.getTotalElements()));
    }
}
