package greenbuildings.idp.rest;

import commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.api.enums.UserLocale;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.idp.dto.UserConfigs;
import greenbuildings.idp.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@RolesAllowed({UserRole.RoleNameConstant.ENTERPRISE_OWNER,
               UserRole.RoleNameConstant.ENTERPRISE_EMPLOYEE,
               UserRole.RoleNameConstant.SYSTEM_ADMIN})
public class UserResource {
    
    private final UserService userService;
    
    @GetMapping("/locale")
    public ResponseEntity<UserConfigs> getLanguage(@AuthenticationPrincipal UserContextData userContextData) {
        var userEntity = userService.findByEmail(userContextData.getUsername()).orElseThrow();
        var userLocaleResponse = UserConfigs
                .builder()
                .language(UserLocale.fromCode(userEntity.getLocale()).getCode())
                .build();
        return ResponseEntity.ok(userLocaleResponse);
    }
    
    @PutMapping("/locale/{locale}")
    public ResponseEntity<Void> changeLanguage(@PathVariable("locale") String locale,
                                               @AuthenticationPrincipal UserContextData userContextData) {
        if (Arrays.stream(UserLocale.values())
                  .map(UserLocale::getCode)
                  .noneMatch(code -> code.equals(locale))
        ) {
            return ResponseEntity.badRequest().build();
        }
        
        var userEntity = userService.findByEmail(userContextData.getUsername()).orElseThrow();
        
        userEntity.setLocale(locale);
        
        userService.update(userEntity);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
}
