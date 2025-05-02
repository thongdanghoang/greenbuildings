package greenbuildings.enterprise.rest;

import commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.services.WalletService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {
    
    private final WalletService walletService;
    
    @GetMapping("/balance")
    @RolesAllowed(UserRole.RoleNameConstant.ENTERPRISE_OWNER)
    public ResponseEntity<Double> getBalance(@AuthenticationPrincipal UserContextData userContextData) {
        return ResponseEntity.ok().body(walletService.getBalance(userContextData.getEnterpriseId().orElseThrow()));
    }
}
