package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.GroupItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupItemRepository extends JpaRepository<GroupItemEntity, UUID> {
    List<GroupItemEntity> findByBuildingGroupId(UUID buildingGroupId);
}
