package greenbuildings.commons.api;

import lombok.Getter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class SagaManager {
    
    protected static final int TRANSACTION_TIMEOUT = 2;
    
    private final ConcurrentHashMap<String, CompletableFuture<Object>> pendingSagaResponses = new ConcurrentHashMap<>();

}
