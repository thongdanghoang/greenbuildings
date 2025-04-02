package greenbuildings.idp.service.impl;

import greenbuildings.commons.api.events.KafkaEventTopicConstant;
import greenbuildings.commons.api.events.PowerBiAccessTokenAuthResult;
import greenbuildings.idp.entity.PowerBiAuthority;
import greenbuildings.idp.exceptions.PowerBiAuthorityNoteAlreadyExists;
import greenbuildings.idp.repository.PowerBiAuthorityRepository;
import greenbuildings.idp.service.PowerBiAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
@Slf4j
public class PowerBiAuthenticationServiceImpl implements PowerBiAuthenticationService {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PowerBiAuthorityRepository repository;
    
    @Override
    public PowerBiAuthority createOrUpdate(PowerBiAuthority powerBiAuthority) {
        if (Objects.nonNull(powerBiAuthority.getId())) {
            return repository.save(powerBiAuthority);
        }
        if (repository.existsByNote((powerBiAuthority.getNote()))) {
            throw new PowerBiAuthorityNoteAlreadyExists();
        }
        powerBiAuthority.setApiKey(UUID.randomUUID().toString());
        return repository.save(powerBiAuthority);
    }
    
    @Override
    public PowerBiAuthority regenerateApiKey(PowerBiAuthority powerBiAuthority) {
        powerBiAuthority.setApiKey(UUID.randomUUID().toString());
        return repository.save(powerBiAuthority);
    }
    
    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PowerBiAuthority> findAllByUser(UUID userId) {
        return repository.findAllByUser_Id(userId);
    }
    
    @Override
    public Optional<PowerBiAuthority> findById(UUID id) {
        return repository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    @KafkaListener(topics = KafkaEventTopicConstant.POWER_BI_AUTHENTICATION_REQUEST_EVENT)
    public void authenticate(Message<String> apiKeyMsg) {
        var correlationId = Objects.requireNonNull(apiKeyMsg.getHeaders().get(KafkaHeaders.CORRELATION_ID, String.class));
        var apiKey = apiKeyMsg.getPayload();
        var hasPermission = checkIfUserHasPermission(apiKey);
        sendAuthResponse(hasPermission, correlationId);
    }
    
    private PowerBiAccessTokenAuthResult checkIfUserHasPermission(String apiKey) {
        return repository.findByApiKey(apiKey)
                         .map(biAuthority -> PowerBiAccessTokenAuthResult
                                 .builder()
                                 .enterpriseId(biAuthority.getEnterpriseId())
                                 .buildings(biAuthority.getBuildings())
                                 .scope(biAuthority.getScope())
                                 .build())
                         .orElse(PowerBiAccessTokenAuthResult.builder().build()); // payload can't be null, if enterpriseId null => no permission
    }
    
    private void sendAuthResponse(PowerBiAccessTokenAuthResult result, String correlationId) {
        var message = MessageBuilder
                .withPayload(result)
                .setHeader(KafkaHeaders.TOPIC, KafkaEventTopicConstant.POWER_BI_AUTHENTICATION_RESPONSE_EVENT)
                .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                .build();
        kafkaTemplate.send(message);
    }
}
