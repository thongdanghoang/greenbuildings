package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.enterprise.dtos.InvitationDTO;
import greenbuildings.enterprise.dtos.InvitationResponseDTO;
import greenbuildings.enterprise.dtos.InvitationSearchCriteria;
import greenbuildings.enterprise.entities.InvitationEntity;
import greenbuildings.enterprise.mappers.InvitationMapper;
import greenbuildings.enterprise.services.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/invitation")
@RequiredArgsConstructor
public class InvitationController {
    
    private final InvitationService invitationService;
    private final InvitationMapper invitationMapper;
    
    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<InvitationDTO>> searchActivityType(@RequestBody SearchCriteriaDTO<InvitationSearchCriteria> searchCriteria) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = invitationService.search(searchCriteria, pageable);
        return ResponseEntity.ok(CommonMapper.toSearchResultDTO(searchResults, invitationMapper::toDTO));
    }
    
    @GetMapping("/find-by-email")
    public ResponseEntity<List<InvitationDTO>> findAllByEmail(@AuthenticationPrincipal UserContextData userContextData) {
        List<InvitationEntity> rs = invitationService.findAllByEmail(userContextData.getUsername());
        return ResponseEntity.ok(rs.stream().map(invitationMapper::toDTO).toList());
    }
    
    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody InvitationResponseDTO invitationDTO) {
         invitationService.updateStatus(invitationDTO);
        return ResponseEntity.noContent().build();
    }
}
