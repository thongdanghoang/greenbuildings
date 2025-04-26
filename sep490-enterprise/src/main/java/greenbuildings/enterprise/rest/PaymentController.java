package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.dtos.PaymentCriteriaDTO;
import greenbuildings.enterprise.dtos.PaymentDTO;
import greenbuildings.enterprise.dtos.PaymentDetailDTO;
import greenbuildings.enterprise.entities.PaymentEntity;
import greenbuildings.enterprise.mappers.PaymentMapper;
import greenbuildings.enterprise.services.PaymentService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
public class PaymentController {
    
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;
    
    @PostMapping("/search")
    @RolesAllowed(UserRole.RoleNameConstant.ENTERPRISE_OWNER)
    public ResponseEntity<SearchResultDTO<PaymentDTO>> searchPayment(@RequestBody SearchCriteriaDTO<PaymentCriteriaDTO> searchCriteria) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = paymentService.search(searchCriteria, pageable);
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        paymentMapper::paymentEntityToPaymentDTO));
    }
    
    @PostMapping("/{id}")
    @RolesAllowed({UserRole.RoleNameConstant.ENTERPRISE_OWNER})
    public ResponseEntity<PaymentDTO> createPayment(@RequestHeader("Origin") String origin, @PathVariable UUID id) {
        PaymentEntity payment = paymentService.createPayment(
                SecurityUtils.getCurrentUserEnterpriseId().orElseThrow(),
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
    @RolesAllowed({UserRole.RoleNameConstant.ENTERPRISE_OWNER})
    public ResponseEntity<Void> updatePayment(@PathVariable Long orderCode) {
        paymentService.updatePaymentInfo(SecurityUtils.getCurrentUserEnterpriseId().orElseThrow(), orderCode);
        return ResponseEntity.noContent().build();
    }
}
