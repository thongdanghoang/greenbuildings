package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.dtos.*;
import greenbuildings.enterprise.entities.PaymentEntity;
import greenbuildings.enterprise.mappers.PaymentMapper;
import greenbuildings.enterprise.services.PaymentService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@RolesAllowed({UserRole.RoleNameConstant.ENTERPRISE_OWNER})
public class PaymentController {
    
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;
    
    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<PaymentDTO>> searchPayment(@RequestBody SearchCriteriaDTO<PaymentCriteriaDTO> searchCriteria,
                                                                     @AuthenticationPrincipal UserContextData userContextData) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = paymentService.search(searchCriteria, pageable, userContextData.getEnterpriseId().orElseThrow());
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        paymentMapper::paymentEntityToPaymentDTO));
    }
    
    @PostMapping("/{id}")
    public ResponseEntity<PaymentDTO> createPayment(@RequestHeader("Origin") String origin,
                                                    @PathVariable UUID id,
                                                    @AuthenticationPrincipal UserContextData userContextData) {
        PaymentEntity payment = paymentService.createPayment(
                userContextData.getEnterpriseId().orElseThrow(),
                id,
                origin);
        return ResponseEntity.ok(paymentMapper.paymentEntityToPaymentDTO(payment));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDetailDTO> findById(@PathVariable UUID id) {
        PaymentDetailDTO paymentDetailDTO = paymentService
                .findById(id)
                .map(paymentMapper::paymentEntityToPaymentDetailDTO)
                .orElseThrow();
        return ResponseEntity.ok(paymentDetailDTO);
    }
    
    @PutMapping("/{orderCode}")
    public ResponseEntity<Void> updatePayment(@PathVariable Long orderCode,
                                              @AuthenticationPrincipal UserContextData userContextData) {
        paymentService.updatePaymentInfo(userContextData.getEnterpriseId().orElseThrow(), orderCode);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed(UserRole.RoleNameConstant.SYSTEM_ADMIN)
    @PostMapping("/search/enterprise")
    public ResponseEntity<SearchResultDTO<PaymentEnterpriseAdminDTO>> searchEnterpriseAdmin(@RequestBody SearchCriteriaDTO<PaymentAdminCriteria> searchCriteria) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = paymentService.searchEnterpriseAdmin(pageable, searchCriteria.criteria().criteria());
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        paymentMapper::toPaymentEnterpriseAdminDTO));
    }
}
