package greenbuildings.enterprise.repositories;

import java.util.UUID;

public interface EntityGraphBasedRepository<ENTITY> {
    
    ENTITY getWithGraph(UUID primaryKey, Class<ENTITY> entityClass, String graphName);
    
    /**
     * Retrieves an entity by its primary key using a dynamically created entity graph based on attribute names.
     *
     * @param primaryKey    The UUID primary key of the entity to retrieve
     * @param entityClass   The class of the entity
     * @param attributeName The names of attributes to include in the entity graph
     * @return The entity if found, null otherwise
     * @throws IllegalArgumentException if primaryKey or entityClass is null or if attributeName is empty
     */
    ENTITY getWithGraph(UUID primaryKey, Class<ENTITY> entityClass, String... attributeName);
}
