package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.BuildingGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BuildingGroupRepository extends JpaRepository<BuildingGroupEntity, UUID> {
}
