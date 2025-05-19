package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.springfw.impl.securities.UserContextData;
import greenbuildings.enterprise.dtos.EnterpriseDTO;
import greenbuildings.enterprise.dtos.EnterpriseDetailDTO;
import greenbuildings.enterprise.dtos.EnterpriseSearchCriteria;
import greenbuildings.enterprise.mappers.EnterpriseMapper;
import greenbuildings.enterprise.services.EnterpriseService;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
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
    public ResponseEntity<EnterpriseDetailDTO> getEnterpriseProfile(@AuthenticationPrincipal UserContextData userContextData) {
        var enterpriseId = userContextData.getEnterpriseId().orElseThrow();
        var enterpriseDetail = service.getEnterpriseDetail(enterpriseId);
        return ResponseEntity.ok(enterpriseDetail);
    }
    
    @GetMapping("/search-by-building/{buildingId}")
    @RolesAllowed({UserRole.RoleNameConstant.TENANT})
    public ResponseEntity<EnterpriseDetailDTO> getEnterpriseProfile(@PathVariable UUID buildingId) {
        var enterpriseDetail = service.getEnterpriseDetailByBuildingId(buildingId);
        return ResponseEntity.ok(mapper.toEnterpriseDetailDTO(enterpriseDetail));
    }
    
    @PutMapping("/profile")
    public ResponseEntity<Void> updateEnterpriseProfile(
            @AuthenticationPrincipal UserContextData userContextData,
            @RequestBody EnterpriseDetailDTO dto) {
        var enterpriseId = userContextData.getEnterpriseId().orElseThrow();
        // Ensure the ID in the DTO matches the authenticated enterprise ID
        if (!Objects.equals(dto.id(), enterpriseId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        service.updateEnterpriseDetail(dto);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/search")
    @RolesAllowed({UserRole.RoleNameConstant.SYSTEM_ADMIN})
    public ResponseEntity<SearchResultDTO<EnterpriseDTO>> search(@RequestBody SearchCriteriaDTO<EnterpriseSearchCriteria> searchCriteria) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = service.search(searchCriteria, pageable);
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        mapper::toDTO));
    }
}
