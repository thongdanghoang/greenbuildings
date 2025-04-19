package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.ActivityTypeEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityTypeEntity, UUID> {
    List<ActivityTypeEntity> findByTenantId(UUID tenantId);

    @Query("""
            SELECT e.id
            FROM ActivityTypeEntity e
            WHERE (LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')) AND e.tenant.id = :tenantId )
            """
    )
    Page<UUID> findByName(String name, Pageable pageable, UUID tenantId);

 List<ActivityTypeEntity> findAllById(@NotNull Iterable<UUID> ids);
} 