package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.dtos.TransactionDTO;
import greenbuildings.enterprise.mappers.TransactionMapper;
import greenbuildings.enterprise.services.TransactionService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
@RolesAllowed({
        UserRole.RoleNameConstant.ENTERPRISE_OWNER
})
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    
    @GetMapping("/building/{id}")
    public ResponseEntity<List<TransactionDTO>> findAllByBuilding(@PathVariable UUID id,
                                                                  @AuthenticationPrincipal UserContextData userContextData) {
        return ResponseEntity.ok(transactionService
                                         .getTransactions(id, userContextData.getEnterpriseId().orElseThrow())
                                         .stream()
                                         .map(transactionMapper::toDTO)
                                         .toList());
    }
    
    @PostMapping("/search/building/{id}")
    public ResponseEntity<SearchResultDTO<TransactionDTO>> search(@RequestBody SearchCriteriaDTO<Void> searchCriteria,
                                                                  @PathVariable UUID id,
                                                                  @AuthenticationPrincipal UserContextData userContextData) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = transactionService
                .search(id, pageable, userContextData.getEnterpriseId().orElseThrow());
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        transactionMapper::toDTO));
    }
    
}
