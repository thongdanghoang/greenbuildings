package greenbuildings.idp.configs;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import commons.springfw.impl.filters.MonitoringFilter;
import commons.springfw.impl.securities.JwtAuthenticationConverter;
import greenbuildings.idp.service.impl.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.encrypt.KeyStoreKeyFactory;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfig {
    
    @Value("${spring.security.keystore.alias}")
    private String keystoreAlias;
    
    @Value("${spring.security.keystore.key-id}")
    private String keyId;
    
    private final JwtAuthenticationConverter converter;
    private final KeyStoreKeyFactory keyStoreKeyFactory;
    
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        // reference: https://docs.spring.io/spring-authorization-server/reference/guides/how-to-userinfo.html
        var authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer
                .authorizationServer()
                .oidc(Customizer.withDefaults());
        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling(exceptions -> exceptions
                                           .defaultAuthenticationEntryPointFor(
                                                   new LoginUrlAuthenticationEntryPoint("/login"),
                                                   new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                                                                              )
                                  );
        return http.cors(Customizer.withDefaults()).build();
    }
    
    @Bean
    @Order(2)
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(
                        resourceServer -> resourceServer.jwt(
                                jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(converter)
                                                            )
                                     )
                .addFilterAfter(new MonitoringFilter(), SecurityContextHolderFilter.class);
        return http.build();
    }
    
    @Bean
    @Order(3)
    public SecurityFilterChain openapiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/v3/api-docs/**")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
    
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        var keyPair = keyStoreKeyFactory.getKeyPair(keystoreAlias);
        var publicKey = (RSAPublicKey) keyPair.getPublic();
        var privateKey = (RSAPrivateKey) keyPair.getPrivate();
        var rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(keyId)
                .build();
        var jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }
    
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(UserInfoService userInfoService) {
        return context -> {
            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                var oidcUserInfo = userInfoService.loadUser(context.getPrincipal().getName());
                context.getClaims().claims(claimsConsumer -> claimsConsumer.putAll(oidcUserInfo.getClaims()));
            } else if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                var customClaims = userInfoService
                        .getCustomClaimsForJwtAuthenticationToken(context.getPrincipal().getName());
                context.getClaims().claims(claimsConsumer -> claimsConsumer.putAll(customClaims));
            }
        };
    }
    
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
}
