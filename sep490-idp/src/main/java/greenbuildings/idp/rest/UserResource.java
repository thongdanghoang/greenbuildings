package greenbuildings.idp.rest;

import greenbuildings.commons.api.security.UserRole;
import greenbuildings.idp.dto.EnterpriseUserDetailsDTO;
import greenbuildings.idp.dto.UserConfigs;
import greenbuildings.idp.mapper.EnterpriseUserMapper;
import greenbuildings.idp.service.UserService;

import commons.springfw.impl.UserLanguage;
import commons.springfw.impl.securities.UserContextData;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@RolesAllowed({UserRole.RoleNameConstant.ENTERPRISE_OWNER,
               UserRole.RoleNameConstant.TENANT,
               UserRole.RoleNameConstant.BASIC_USER,
               UserRole.RoleNameConstant.SYSTEM_ADMIN})
public class UserResource {
    
    private final UserService userService;
    private final EnterpriseUserMapper userMapper;
    
    @GetMapping("/language")
    public ResponseEntity<UserConfigs> getLanguage(@AuthenticationPrincipal UserContextData userContextData) {
        var userEntity = userService.findById(userContextData.getId()).orElseThrow();
        var userLocaleResponse = UserConfigs
                .builder()
                .language(userEntity.getLocale())
                .build();
        return ResponseEntity.ok(userLocaleResponse);
    }
    
    @PutMapping("/language")
    public ResponseEntity<Void> changeLanguage(@RequestBody UpdateUserLocale body,
                                               @AuthenticationPrincipal UserContextData userContextData) {
        var userEntity = userService.findById(userContextData.getId()).orElseThrow();
        
        userEntity.setLocale(body.language().getCode());
        
        userService.update(userEntity);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    public record UpdateUserLocale(UserLanguage language) {
    }
    
    @GetMapping
    public ResponseEntity<EnterpriseUserDetailsDTO> getEnterpriseUserDetail(@AuthenticationPrincipal UserContextData userContextData) {
        var userDetailsEntity = userService.getUserDetail(userContextData.getId());
        var userDetails = userMapper.userEntityToBasicEnterpriseUserDetailDTO(userDetailsEntity);
        return ResponseEntity.ok(userDetails);
    }
    
}
