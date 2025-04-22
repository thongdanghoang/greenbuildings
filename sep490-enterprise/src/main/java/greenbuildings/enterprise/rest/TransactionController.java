package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.dtos.TransactionDTO;
import greenbuildings.enterprise.mappers.TransactionMapper;
import greenbuildings.enterprise.services.TransactionService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<TransactionDTO>> findAllByBuilding(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionService.getTransactions(id).stream().map(transactionMapper::toDTO).toList());
    }

    @PostMapping("/search/building/{id}")
    public ResponseEntity<SearchResultDTO<TransactionDTO>> search(@RequestBody SearchCriteriaDTO<Void> searchCriteria, @PathVariable UUID id) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = transactionService.search(id, pageable);
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        transactionMapper::toDTO));
    }

}
