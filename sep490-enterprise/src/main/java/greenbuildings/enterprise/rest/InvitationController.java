package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.dtos.InvitationDTO;
import greenbuildings.enterprise.dtos.InvitationResponseDTO;
import greenbuildings.enterprise.dtos.InvitationSearchCriteria;
import greenbuildings.enterprise.mappers.InvitationMapper;
import greenbuildings.enterprise.services.InvitationService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/invitation")
@RequiredArgsConstructor
public class InvitationController {
    
    private final InvitationService invitationService;
    private final InvitationMapper invitationMapper;
    
    @PostMapping("/search")
    @RolesAllowed({UserRole.RoleNameConstant.ENTERPRISE_OWNER})
    public ResponseEntity<SearchResultDTO<InvitationDTO>> searchInvitation(
            @RequestBody SearchCriteriaDTO<InvitationSearchCriteria> searchCriteria,
            @AuthenticationPrincipal UserContextData userContextData) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var enterpriseId = userContextData.getEnterpriseId().orElseThrow();
        var criteria = searchCriteria.criteria().toBuilder().enterpriseId(enterpriseId).build();
        var searchResults = invitationService.search(criteria, pageable);
        return ResponseEntity.ok(CommonMapper.toSearchResultDTO(searchResults, invitationMapper::toDTO));
    }
    
    @GetMapping("/find-by-email")
    public ResponseEntity<List<InvitationDTO>> findAllByEmail(@AuthenticationPrincipal UserContextData userContextData) {
        var invitations = invitationService.findAllByEmail(userContextData.getEmail());
        return ResponseEntity.ok(invitations.stream().map(invitationMapper::toDTO).toList());
    }
    
    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody InvitationResponseDTO invitationDTO) {
        invitationService.updateStatus(invitationDTO);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/pending/building-group/{id}")
    public ResponseEntity<InvitationDTO> fetchPendingInvitation(@PathVariable UUID id) {
        var invitation = invitationService.findPendingInvitationByBuildingGroupId(id);
        return ResponseEntity.ok(invitationMapper.toDTO(invitation));
    }
}
