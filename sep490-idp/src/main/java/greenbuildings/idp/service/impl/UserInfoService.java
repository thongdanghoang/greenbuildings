package greenbuildings.idp.service.impl;

import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.entity.UserPermissionEntity;
import greenbuildings.idp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    
    private final UserRepository userRepository;
    
    public OidcUserInfo loadUser(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow();
        return new OidcUserInfo(buildUserClaims(user));
    }
    
    private Map<String, Object> buildUserClaims(UserEntity user) {
        return OidcUserInfo.builder()
                           .subject(user.getId().toString())
                           .givenName(user.getFirstName())
                           .familyName(user.getLastName())
                           .email(user.getEmail())
                           .emailVerified(user.isEmailVerified())
                           .phoneNumber(user.getPhone())
                           .phoneNumberVerified(false)
                           .build()
                           .getClaims();
    }
    
    
    public Map<String, Object> getCustomClaimsForJwtAuthenticationToken(String email) {
        var claims = new HashMap<String, Object>();
        var user = userRepository.findByEmail(email).orElseThrow();
        
        var authorities = user
                .getAuthorities().stream()
                .map(UserPermissionEntity::getRole)
                .distinct()
                .map(role -> "ROLE_".concat(role.getCode()))
                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
        var permissions = user
                .getAuthorities()
                .stream()
                .map(a -> "%s:%s".formatted(a.getRole().getCode(), a.getReferenceId()))
                .toList();
        claims.put("sub", user.getId().toString());
        claims.put("email", user.getEmail());
        claims.put("permissions", permissions);
        claims.put("authorities", authorities);
        return claims;
    }
}
