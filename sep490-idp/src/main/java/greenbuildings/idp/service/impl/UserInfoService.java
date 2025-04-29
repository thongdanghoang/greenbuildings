package greenbuildings.idp.service.impl;

import greenbuildings.commons.api.dto.auth.BuildingPermissionDTO;
import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.repository.BuildingPermissionRepository;
import greenbuildings.idp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    
    private final UserRepository userRepository;
    private final BuildingPermissionRepository buildingPermissionRepository;
    
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
        Map<String, Object> claims = new HashMap<>();
        var user = userRepository.findByEmail(email).orElseThrow();
        
        Set<String> authorities = Stream.of(user.getRole()).map(r -> "ROLE_".concat(r.getCode())).collect(
                Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
        
        var buildingPermissions = buildingPermissionRepository
                .findAllByUserId(user.getId())
                .stream()
                .map(buildingPermission -> new BuildingPermissionDTO(
                        buildingPermission.getBuilding(),
                        buildingPermission.getRole()
                ))
                .toList();
        claims.put("enterpriseId", Optional.ofNullable(user.getEnterpriseId())
                                           .map(UUID::toString)
                                           .orElse(null));
        claims.put("permissions", buildingPermissions);
        claims.put("sub", user.getId().toString());
        claims.put("email", user.getEmail());
        claims.put("authorities", authorities);
        return claims;
    }
}
