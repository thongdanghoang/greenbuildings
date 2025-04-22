package greenbuildings.enterprise.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")  // Add this to your application.properties/yaml
    private String idpServiceUrl;
    
    @Bean
    public WebClient idpWebClient() {
        return WebClient.builder().baseUrl(idpServiceUrl).build();
    }
}
