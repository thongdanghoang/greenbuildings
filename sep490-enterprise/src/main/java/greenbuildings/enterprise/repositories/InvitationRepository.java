package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.InvitationEntity;
import greenbuildings.enterprise.enums.InvitationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationEntity, UUID> {
    
    List<InvitationEntity> findByEmailOrderByCreatedByDesc(String email);
    
    @Query("""
            SELECT i.id
            FROM InvitationEntity i
            WHERE i.buildingGroup.building.enterprise.id = :enterpriseId
              AND (:buildingId IS NULL OR i.buildingGroup.building.id = :buildingId)
              AND (:buildingGroupId IS NULL OR i.buildingGroup.id = :buildingGroupId)
              AND (:status IS NULL OR i.status = :status)
              AND (:tenantEmail IS NULL OR :tenantEmail = '' OR i.email ILIKE '%' || :tenantEmail || '%')
            """)
    Page<UUID> search(@Param("enterpriseId") UUID enterpriseId,
                      @Param("buildingId") UUID buildingId,
                      @Param("buildingGroupId") UUID buildingGroupId,
                      @Param("status") InvitationStatus status,
                      @Param("tenantEmail") String tenantEmail,
                      Pageable pageable);
    
    boolean existsByBuildingGroupBuildingIdAndEmailAndStatus(UUID buildingId, String email, InvitationStatus status);
    
}
