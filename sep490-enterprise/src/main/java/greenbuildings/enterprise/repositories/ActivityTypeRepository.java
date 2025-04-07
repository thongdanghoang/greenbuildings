package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.ActivityTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityTypeEntity, UUID> {
    List<ActivityTypeEntity> findByBuildingId(UUID buildingId);
} 