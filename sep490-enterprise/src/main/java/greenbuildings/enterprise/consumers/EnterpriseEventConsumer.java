package greenbuildings.enterprise.consumers;

import commons.springfw.impl.securities.KafkaSecurityConfig;
import greenbuildings.commons.api.events.PendingEnterpriseRegisterEvent;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.utils.MDCContext;
import greenbuildings.enterprise.mappers.EnterpriseMapper;
import greenbuildings.enterprise.producers.EnterpriseEventProducer;
import greenbuildings.enterprise.services.EnterpriseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class EnterpriseEventConsumer extends KafkaSecurityConfig {
    
    private final EnterpriseEventProducer enterpriseEventProducer;
    private final EnterpriseService enterpriseService;
    private final EnterpriseMapper enterpriseMapper;
    
    public EnterpriseEventConsumer(HttpServletRequest request,
                                   HttpServletResponse response,
                                   JwtDecoder jwtDecoder,
                                   Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter,
                                   EnterpriseEventProducer enterpriseEventProducer,
                                   EnterpriseService enterpriseService,
                                   EnterpriseMapper enterpriseMapper) {
        super(request, response, jwtDecoder, jwtAuthenticationConverter);
        this.enterpriseEventProducer = enterpriseEventProducer;
        this.enterpriseService = enterpriseService;
        this.enterpriseMapper = enterpriseMapper;
    }
    
    @KafkaListener(topics = PendingEnterpriseRegisterEvent.TOPIC)
    public void handleEnterpriseRegisterEvent(ConsumerRecord<String, Object> event) {
        var correlationId = getCorrelationId(event);
        if (event.value() instanceof PendingEnterpriseRegisterEvent enterpriseCreateEvent) {
            try {
                var enterprise = enterpriseMapper.createEnterprise(enterpriseCreateEvent);
                var enterpriseId = enterpriseService.createEnterprise(enterprise);
                enterpriseEventProducer.publishEnterpriseCreatedEvent(correlationId, enterpriseId);
            } catch (Exception exception) {
                if (exception instanceof BusinessException businessException) {
                    enterpriseEventProducer.publishEnterpriseCreationFailedEvent(correlationId, businessException);
                }
                enterpriseEventProducer.publishEnterpriseCreationFailedEvent(correlationId,
                                                                             new BusinessException("enterprise", "UserRegister.UnexpectedError", List.of()));
            }
        }
    }
    
    private String getCorrelationId(ConsumerRecord<String, Object> consumerRecord) {
        return StringUtils.toEncodedString(consumerRecord.headers().lastHeader(MDCContext.CORRELATION_ID).value(), StandardCharsets.UTF_8);
    }
    
    @KafkaListener(topics = "oauth2")
    public void handleTokenEvent(ConsumerRecord<String, Object> event) {
        processAuthorization(new String(event.headers().lastHeader(BEARER_TOKEN_HEADER_NAME).value()));
    }
    
}
