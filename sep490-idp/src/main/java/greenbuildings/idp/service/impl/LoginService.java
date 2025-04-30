package greenbuildings.idp.service.impl;

import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.entity.UserPermissionEntity;
import greenbuildings.idp.security.MvcUserContextData;
import greenbuildings.idp.security.PasskeyAuthenticationToken;
import greenbuildings.idp.utils.IdpSecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class LoginService {
    
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    
    public void login(UserEntity user) {
        var permissions = user
                .getAuthorities()
                .stream()
                .collect(Collectors.toMap(
                        UserPermissionEntity::getRole,
                        UserPermissionEntity::getReferenceId,
                        (existing, replacement) -> existing
                                         ));
        var userContextData = new MvcUserContextData(user, IdpSecurityUtils.getAuthoritiesFromUserRole(user), permissions);
        var auth = new PasskeyAuthenticationToken(userContextData);
        IdpSecurityUtils.storeAuthenticationToContext(auth, request, response);
    }
}
