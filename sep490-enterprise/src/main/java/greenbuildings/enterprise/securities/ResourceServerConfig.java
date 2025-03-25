package greenbuildings.enterprise.securities;

import commons.springfw.impl.filters.MonitoringFilter;
import commons.springfw.impl.securities.JwtAuthenticationConverter;
import greenbuildings.enterprise.filters.PowerBiBasicAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

@Configuration
@EnableMethodSecurity(jsr250Enabled = true)
@RequiredArgsConstructor
public class ResourceServerConfig {
    
    private final JwtAuthenticationConverter converter;
    
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(c -> c.jwt(j -> j.jwtAuthenticationConverter(converter)))
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/actuator/health/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterAfter(new MonitoringFilter(), SecurityContextHolderFilter.class)
                .cors(Customizer.withDefaults())
                .build();
    }
    
    @Bean
    @Order(1)
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/power-bi/**")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAfter(new MonitoringFilter(), SecurityContextHolderFilter.class)
                .addFilterAfter(new PowerBiBasicAuthenticationFilter(), MonitoringFilter.class);
        return http.build();
    }
    
}
