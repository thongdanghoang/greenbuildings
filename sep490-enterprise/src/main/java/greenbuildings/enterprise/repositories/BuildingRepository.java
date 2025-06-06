package greenbuildings.enterprise.repositories;

import greenbuildings.commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.BuildingEntity;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BuildingRepository extends AbstractBaseRepository<BuildingEntity>, EntityGraphBasedRepository<BuildingEntity> {
    
    Page<BuildingEntity> findByEnterpriseId(UUID enterpriseId, Pageable pageable);
    
    Optional<BuildingEntity> findByIdAndEnterpriseId(UUID id, UUID enterpriseId);
    
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BuildingEntity b WHERE b.name = :name AND b.enterprise.id = :enterpriseId")
    boolean existsByNameBuildingInEnterprise(@NotBlank String name, UUID enterpriseId);
}
