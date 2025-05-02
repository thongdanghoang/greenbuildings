package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.TenantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TenantRepository extends JpaRepository<TenantEntity, UUID> {
    
    @Query("SELECT t.id FROM TenantEntity t")
    Page<UUID> findAllProjectToUUID(Pageable pageable);
    
    List<TenantEntity> findAllByIdIn(Set<UUID> IDs);
    
    @Query("""
                SELECT DISTINCT t
                FROM TenantEntity t
                JOIN BuildingGroupEntity groups on t.id = groups.tenant.id
                JOIN BuildingEntity building on groups.building.id = building.id
                WHERE building.enterprise.id = :enterpriseId
                    AND t.email ILIKE '%' || :tenantEmail || '%'
            """)
    Page<TenantEntity> findByEnterpriseId(@Param("enterpriseId") UUID enterpriseId,
                                          @Param("tenantEmail") String tenantEmail,
                                          Pageable pageable);
    
    Page<TenantEntity> findByEmailContainsIgnoreCase(String email, Pageable pageable);
}
