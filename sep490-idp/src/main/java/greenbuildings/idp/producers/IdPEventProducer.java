package greenbuildings.idp.producers;

import commons.springfw.impl.securities.KafkaSecurityConfig;
import greenbuildings.commons.api.events.PendingEnterpriseRegisterEvent;
import greenbuildings.commons.api.utils.MDCContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class IdPEventProducer extends KafkaSecurityConfig {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public IdPEventProducer(HttpServletRequest request,
                            HttpServletResponse response,
                            JwtDecoder jwtDecoder,
                            Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter,
                            KafkaTemplate<String, Object> kafkaTemplate) {
        super(request, response, jwtDecoder, jwtAuthenticationConverter);
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public void publishEnterpriseOwnerRegisterEvent(String correlationId, PendingEnterpriseRegisterEvent payload) {
        var msg = new ProducerRecord<String, Object>(PendingEnterpriseRegisterEvent.TOPIC, payload);
        msg.headers().add(MDCContext.CORRELATION_ID, correlationId.getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(msg);
    }
    
    public void publishTestTokenEvent() {
        var msg = new ProducerRecord<String, Object>("oauth2", "Hello worlds!");
        msg.headers().add(BEARER_TOKEN_HEADER_NAME, getBearerToken(SecurityContextHolder.getContext().getAuthentication()));
        kafkaTemplate.send(msg);
    }
    
}
