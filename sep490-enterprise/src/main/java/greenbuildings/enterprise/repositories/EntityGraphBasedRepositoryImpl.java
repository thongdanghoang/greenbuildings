package greenbuildings.enterprise.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Map;
import java.util.UUID;

public class EntityGraphBasedRepositoryImpl<ENTITY> implements EntityGraphBasedRepository<ENTITY> {
    public static final String ENTITY_GRAPH_HINT_PARAM_NAME = "jakarta.persistence.fetchgraph";
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Override
    public ENTITY getWithGraph(UUID primaryKey, Class<ENTITY> entityClass, String graphName) {
        return entityManager.find(
                entityClass,
                primaryKey,
                Map.of(ENTITY_GRAPH_HINT_PARAM_NAME, entityManager.getEntityGraph(graphName))
                                 );
    }
    
    @Override
    public ENTITY getWithGraph(UUID primaryKey, Class<ENTITY> entityClass, String... attributeName) {
        var entityGraph = entityManager.createEntityGraph(entityClass);
        entityGraph.addAttributeNodes(attributeName);
        return entityManager.find(
                entityClass,
                primaryKey,
                Map.of(ENTITY_GRAPH_HINT_PARAM_NAME, entityGraph)
                                 );
    }
}
