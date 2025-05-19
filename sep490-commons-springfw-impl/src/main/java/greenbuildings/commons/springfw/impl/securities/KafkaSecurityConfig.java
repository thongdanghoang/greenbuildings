package greenbuildings.commons.springfw.impl.securities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public abstract class KafkaSecurityConfig {
    
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final JwtDecoder jwtDecoder;
    private final Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();
    private static final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    
    protected static final String BEARER_TOKEN_HEADER_NAME = "Authorization";
    private static final Pattern authorizationPattern = Pattern
            .compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$", Pattern.CASE_INSENSITIVE);
    
    protected byte[] getBearerToken(org.springframework.security.core.Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            return StringSubstitutor
                    .replace("Bearer ${token}", Map
                            .of("token", jwtToken.getToken().getTokenValue()))
                    .getBytes(StandardCharsets.UTF_8);
        }
        throw new IllegalArgumentException("Only extract bearer token from JwtAuthenticationToken");
    }
    
    protected void processAuthorization(String authorizationHeader) {
        String token;
        try {
            token = resolveFromAuthorizationHeader(authorizationHeader);
        } catch (OAuth2AuthenticationException invalid) {
            log.trace("Sending to authentication entry point since failed to resolve bearer token", invalid);
            return;
        }
        if (token == null) {
            log.trace("Did not process request since did not find bearer token");
            return;
        }
        
        BearerTokenAuthenticationToken authenticationRequest = new BearerTokenAuthenticationToken(token);
        try {
            Authentication authenticationResult = authenticate(authenticationRequest);
            SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authenticationResult);
            this.securityContextHolderStrategy.setContext(context);
            securityContextRepository.saveContext(context, request, response);
            if (log.isDebugEnabled()) {
                log.debug("Set SecurityContextHolder to {}", authenticationResult);
            }
        } catch (AuthenticationException failed) {
            this.securityContextHolderStrategy.clearContext();
            log.trace("Failed to process authentication request", failed);
        }
    }
    
    private Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BearerTokenAuthenticationToken bearer = (BearerTokenAuthenticationToken) authentication;
        Jwt jwt = getJwt(bearer);
        AbstractAuthenticationToken token = this.jwtAuthenticationConverter.convert(jwt);
        token.setDetails(bearer.getDetails());
        log.debug("Authenticated token");
        return token;
    }
    
    private Jwt getJwt(BearerTokenAuthenticationToken bearer) {
        try {
            return this.jwtDecoder.decode(bearer.getToken());
        } catch (BadJwtException failed) {
            log.debug("Failed to authenticate since the JWT was invalid");
            throw new InvalidBearerTokenException(failed.getMessage(), failed);
        } catch (JwtException failed) {
            throw new AuthenticationServiceException(failed.getMessage(), failed);
        }
    }
    
    private String resolveFromAuthorizationHeader(String authorization) {
        if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
            return null;
        }
        Matcher matcher = authorizationPattern.matcher(authorization);
        if (!matcher.matches()) {
            BearerTokenError error = BearerTokenErrors.invalidToken("Bearer token is malformed");
            throw new OAuth2AuthenticationException(error);
        }
        return matcher.group("token");
    }
    
}
