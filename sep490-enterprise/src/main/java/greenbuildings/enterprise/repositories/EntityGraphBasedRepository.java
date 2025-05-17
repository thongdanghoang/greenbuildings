package greenbuildings.enterprise.repositories;

import java.util.UUID;

public interface EntityGraphBasedRepository<ENTITY> {
    
    ENTITY getWithGraph(UUID primaryKey, Class<ENTITY> entityClass, String graphName);
    
    ENTITY getWithGraph(UUID primaryKey, Class<ENTITY> entityClass, String... attributeName);
}
