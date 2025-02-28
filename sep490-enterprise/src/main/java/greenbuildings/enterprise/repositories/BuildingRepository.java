package greenbuildings.enterprise.repositories;

import commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.BuildingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BuildingRepository extends AbstractBaseRepository<BuildingEntity> {
    
    Page<BuildingEntity> findByEnterpriseId(UUID enterpriseId, Pageable pageable);
    
    List<BuildingEntity> findAllByEnterpriseId(UUID enterpriseId);
    
    Optional<BuildingEntity> findByIdAndEnterpriseId(UUID id, UUID enterpriseId);
    
}
