package greenbuildings.enterprise.validators;

import greenbuildings.commons.api.events.KafkaEventTopicConstant;
import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.commons.api.utils.MDCContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
@Slf4j
public class PowerBiSecurityCheckerBean {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    protected static final int TRANSACTION_TIMEOUT = 2;
    private final ConcurrentHashMap<String, CompletableFuture<Boolean>> authResponses = new ConcurrentHashMap<>();
    
    public boolean checkIfUserHasPermission(String apiKey) {
        var future = new CompletableFuture<Boolean>();
        var correlationId = MDC.get(MDCContext.CORRELATION_ID);
        authResponses.put(correlationId, future);
        sendAuthRequest(apiKey);
        
        try { // Wait synchronously for response
            return future.get(TRANSACTION_TIMEOUT, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new TechnicalException("Request timeout", e);
        } catch (ExecutionException e) {
            throw new TechnicalException("Error while waiting for response", e);
        } catch (InterruptedException e) {
            /* Clean up whatever needs to be handled before interrupting  */
            log.warn("Interrupted!", e);
            Thread.currentThread().interrupt();
        } finally {
            authResponses.remove(correlationId);
        }
        return false;
    }
    
    public void sendAuthRequest(String apiKey) {
        var correlationId = MDC.get(MDCContext.CORRELATION_ID);
        
        var message = MessageBuilder
                .withPayload(apiKey)
                .setHeader(KafkaHeaders.TOPIC, KafkaEventTopicConstant.POWER_BI_AUTHENTICATION_REQUEST_EVENT)
                .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                .build();
        
        kafkaTemplate.send(message);
    }
    
    @KafkaListener(topics = KafkaEventTopicConstant.POWER_BI_AUTHENTICATION_RESPONSE_EVENT)
    public void handleAuthResponse(Message<Boolean> authResponseMsg) {
        var correlationId = Objects.requireNonNull(authResponseMsg.getHeaders().get(KafkaHeaders.CORRELATION_ID, String.class));
        var authResponse = authResponseMsg.getPayload();
        var future = authResponses.get(correlationId);
        if (Objects.nonNull(future)) {
            future.complete(authResponse);
        }
    }
}
