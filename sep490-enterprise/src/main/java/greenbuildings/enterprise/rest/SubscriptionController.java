package greenbuildings.enterprise.rest;


import commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.dtos.CreditConvertRatioDTO;
import greenbuildings.enterprise.dtos.SubscribeRequestDTO;
import greenbuildings.enterprise.mappers.CreditConvertRatioMapper;
import greenbuildings.enterprise.services.SubscriptionService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionController {
    
    private final SubscriptionService subscriptionService;
    private final CreditConvertRatioMapper creditConvertRatioMapper;
    
    
    @GetMapping("/convert-ratio")
    public ResponseEntity<List<CreditConvertRatioDTO>> getCreditConvertRatio() {
        List<CreditConvertRatioDTO> list = subscriptionService.getCreditConvertRatios().stream().map(creditConvertRatioMapper::toDTO).toList();
        return ResponseEntity.ok(list);
    }
    
    @PostMapping
    @RolesAllowed(value = {UserRole.RoleNameConstant.ENTERPRISE_OWNER})
    public ResponseEntity<Void> subscribe(@RequestBody SubscribeRequestDTO request,
                                          @AuthenticationPrincipal UserContextData userContextData) {
        subscriptionService.subscribe(request, userContextData.getEnterpriseId().orElseThrow());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @GetMapping("/convert-ratio/{id}")
    public ResponseEntity<CreditConvertRatioDTO> getCreditConvertRatioDetail(@PathVariable UUID id) {
        var creditConvertRatio = subscriptionService.getCreditConvertRatioDetail(id);
        return ResponseEntity.ok(creditConvertRatioMapper.toDTO(creditConvertRatio));
    }
    
    @PostMapping("/convert-ratio/update")
    public ResponseEntity<Void> updateCreditConvertRatio(@RequestBody CreditConvertRatioDTO creditConvertRatioDTO) {
        subscriptionService.updateCreditConvertRatio(creditConvertRatioDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
}
