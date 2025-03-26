package greenbuildings.enterprise.producers;

import greenbuildings.commons.api.events.CommitEnterpriseRegisterEvent;
import greenbuildings.commons.api.events.RollbackEnterpriseRegisterEvent;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.utils.MDCContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnterpriseEventProducer {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public void publishEnterpriseCreatedEvent(String correlationId, UUID enterpriseId) {
        var msg = new ProducerRecord<String, Object>(CommitEnterpriseRegisterEvent.TOPIC, new CommitEnterpriseRegisterEvent(enterpriseId));
        msg.headers().add(MDCContext.CORRELATION_ID, correlationId.getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(msg);
    }
    
    public void publishEnterpriseCreationFailedEvent(String correlationId, BusinessException exception) {
        var event = RollbackEnterpriseRegisterEvent.builder()
                                                   .field(exception.getField())
                                                   .i18nKey(exception.getI18nKey())
                                                   .args(exception.getArgs())
                                                   .build();
        var msg = new ProducerRecord<String, Object>(RollbackEnterpriseRegisterEvent.TOPIC, event);
        msg.headers().add(MDCContext.CORRELATION_ID, correlationId.getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(msg);
    }
    
}
