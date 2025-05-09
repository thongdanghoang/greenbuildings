package greenbuildings.enterprise.repositories;

import greenbuildings.commons.api.utils.CommonConstant;
import greenbuildings.enterprise.entities.TenantEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
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
                JOIN t.buildingGroups groups
                JOIN groups.building building
                WHERE building.enterprise.id = :enterpriseId
                    AND t.email ILIKE '%' || :tenantEmail || '%'
            """)
    Page<TenantEntity> findByEnterpriseId(UUID enterpriseId,
                                          String tenantEmail,
                                          Pageable pageable);
    
    Page<TenantEntity> findByEmailContainsIgnoreCase(String email, Pageable pageable);

    Optional<TenantEntity> findByEmail(@NotBlank @Pattern(regexp = CommonConstant.EMAIL_PATTERN) String email);
}
