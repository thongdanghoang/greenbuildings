package commons.springfw.impl.securities;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import greenbuildings.commons.api.dto.auth.BuildingPermissionDTO;
import greenbuildings.commons.api.security.BuildingPermissionRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationConverter
        implements Converter<Jwt, JwtAuthenticationTokenDecorator> {
    
    @Override
    public JwtAuthenticationTokenDecorator convert(Jwt source) {
        var email = Objects.requireNonNull(source.getClaims().get("sub")).toString();
        if (StringUtils.isBlank(email)) {
            return null;
        }
        
        var authoritiesClaim = Objects.requireNonNull(source.getClaims().get("authorities"));
        Set<SimpleGrantedAuthority> authorities = Collections.emptySet();
        if (authoritiesClaim instanceof List<?> authoritiesLists) {
            authorities = authoritiesLists
                    .stream()
                    .filter(String.class::isInstance)
                    .map(authority -> new SimpleGrantedAuthority((String) authority))
                    .collect(Collectors.toUnmodifiableSet());
        }
        
        var buildingPermissionsClaim = Optional.ofNullable(source.getClaims().get("permissions")).orElse(Collections.emptyList());
        List<BuildingPermissionDTO> buildingPermissions = Collections.emptyList();
        if (buildingPermissionsClaim instanceof List<?> buildingPermissionsList) {
            buildingPermissions = buildingPermissionsList
                    .stream()
                    .filter(LinkedTreeMap.class::isInstance)
                    .map(permission -> {
                        var permissionMap = (LinkedTreeMap) permission;
                        UUID uuid = Optional.ofNullable(permissionMap.get(BuildingPermissionDTO.Fields.buildingId))
                                            .map(String.class::cast)
                                            .map(UUID::fromString)
                                            .orElse(null);
                        return new BuildingPermissionDTO(
                                uuid,
                                BuildingPermissionRole.valueOf((String) permissionMap.get(BuildingPermissionDTO.Fields.role))
                        );
                    })
                    .toList();
        }
        var enterpriseId = Optional
                .ofNullable(source.getClaims().get("enterpriseId"))
                .map(Object::toString)
                .map(UUID::fromString)
                .orElse(null);
        
        return new JwtAuthenticationTokenDecorator(
                source,
                new UserContextData(
                        email,
                        enterpriseId,
                        StringUtils.EMPTY,
                        List.copyOf(authorities),
                        List.copyOf(buildingPermissions)
                )
        );
    }
}
