package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.TenantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TenantRepository extends JpaRepository<TenantEntity, UUID> {
    
    @Query("""
            SELECT t
            FROM TenantEntity t
            LEFT JOIN t.activityTypes
            WHERE t.id = :tenantId
            """)
    @EntityGraph(attributePaths = {TenantEntity.Fields.activityTypes})
    Optional<TenantEntity> findByIdWithActivityTypes(UUID tenantId);
    
    @Query("SELECT t.id FROM TenantEntity t")
    Page<UUID> findAllProjectToUUID(Pageable pageable);
    
    @EntityGraph(attributePaths = {TenantEntity.Fields.activityTypes})
    List<TenantEntity> findAllByIdIn(Set<UUID> IDs);
    
    @Query("SELECT t FROM TenantEntity t")
    @EntityGraph(attributePaths = {TenantEntity.Fields.activityTypes})
    List<TenantEntity> findALlWithActivityTypes();
}
