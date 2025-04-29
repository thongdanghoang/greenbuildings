package greenbuildings.enterprise.securities;

import commons.springfw.impl.filters.MonitoringFilter;
import commons.springfw.impl.securities.JwtAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(c -> c.jwt(j -> j.jwtAuthenticationConverter(converter)))
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/v3/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/power-bi/**").permitAll()
                        .requestMatchers("/actuator/health/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterAfter(new MonitoringFilter(), SecurityContextHolderFilter.class)
                .cors(Customizer.withDefaults())
                .build();
    }
    
}
