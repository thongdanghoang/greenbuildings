package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.InvitationEntity;
import greenbuildings.enterprise.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationEntity, UUID> {
    
    List<InvitationEntity> findByEmailAndStatusOrderByCreatedByDesc(String email, InvitationStatus status);
    
}
