package greenbuildings.idp.service.impl;

import greenbuildings.idp.entity.UserPermissionEntity;
import greenbuildings.idp.repository.UserRepository;
import greenbuildings.idp.security.MvcUserContextData;
import greenbuildings.idp.utils.IdpSecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) {
        var user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }
        var permissions = user
                .getAuthorities()
                .stream()
                .collect(Collectors.toMap(
                        UserPermissionEntity::getRole,
                        UserPermissionEntity::getReferenceId,
                        (existing, replacement) -> existing
                                         ));
        return new MvcUserContextData(user, IdpSecurityUtils.getAuthoritiesFromUserRole(user), permissions);
    }
}
