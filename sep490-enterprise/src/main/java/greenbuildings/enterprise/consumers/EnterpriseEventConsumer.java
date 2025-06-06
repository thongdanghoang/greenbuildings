package greenbuildings.enterprise.consumers;

import greenbuildings.commons.api.events.PendingEnterpriseRegisterEvent;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.commons.api.utils.MDCContext;
import greenbuildings.commons.springfw.impl.securities.KafkaSecurityConfig;
import greenbuildings.enterprise.mappers.EnterpriseMapper;
import greenbuildings.enterprise.mappers.TenantMapper;
import greenbuildings.enterprise.producers.EnterpriseEventProducer;
import greenbuildings.enterprise.services.EnterpriseService;
import greenbuildings.enterprise.services.TenantService;

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
    private final TenantMapper tenantMapper;
    private final TenantService tenantService;
    
    public EnterpriseEventConsumer(HttpServletRequest request,
                                   HttpServletResponse response,
                                   JwtDecoder jwtDecoder,
                                   Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter,
                                   EnterpriseEventProducer enterpriseEventProducer,
                                   EnterpriseService enterpriseService,
                                   EnterpriseMapper enterpriseMapper, TenantMapper tenantMapper, TenantService tenantService) {
        super(request, response, jwtDecoder, jwtAuthenticationConverter);
        this.enterpriseEventProducer = enterpriseEventProducer;
        this.enterpriseService = enterpriseService;
        this.enterpriseMapper = enterpriseMapper;
        this.tenantMapper = tenantMapper;
        this.tenantService = tenantService;
    }
    
    @KafkaListener(topics = PendingEnterpriseRegisterEvent.TOPIC)
    public void handleEnterpriseRegisterEvent(ConsumerRecord<String, Object> event) {
        var correlationId = getCorrelationId(event);
        if (event.value() instanceof PendingEnterpriseRegisterEvent enterpriseCreateEvent) {
            try {
                if (enterpriseCreateEvent.role() == UserRole.ENTERPRISE_OWNER) {
                    var enterprise = enterpriseMapper.createEnterprise(enterpriseCreateEvent);
                    var enterpriseId = enterpriseService.createEnterprise(enterprise);
                    enterpriseEventProducer.publishEnterpriseCreatedEvent(correlationId, enterpriseId);
                } else if (enterpriseCreateEvent.role() == UserRole.TENANT) {
                    var tenant = tenantMapper.createTenant(enterpriseCreateEvent);
                    var tenantId = tenantService.create(tenant).getId();
                    enterpriseEventProducer.publishEnterpriseCreatedEvent(correlationId, tenantId);
                }
            } catch (Exception exception) {
                log.error("Error while creating enterprise: {}", exception.getMessage(), exception);
                if (exception instanceof BusinessException businessException) {
                    enterpriseEventProducer.publishEnterpriseCreationFailedEvent(correlationId, businessException);
                } else {
                    enterpriseEventProducer.publishEnterpriseCreationFailedEvent(
                            correlationId,
                            new BusinessException("enterprise", "UserRegister.UnexpectedError", List.of())
                                                                                );
                }
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
