package greenbuildings.enterprise.repositories;

import commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.BuildingEntity;
import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BuildingRepository extends AbstractBaseRepository<BuildingEntity> {

    Page<BuildingEntity> findByEnterpriseId(UUID enterpriseId, Pageable pageable);
    
    List<BuildingEntity> findAllByEnterpriseId(UUID enterpriseId);
    
    Optional<BuildingEntity> findByIdAndEnterpriseId(UUID id, UUID enterpriseId);
    
    @EntityGraph(attributePaths = BuildingEntity.Fields.buildingGroups)
    @Query(value = "SELECT b FROM BuildingEntity b where b.id=:id")
    Optional<BuildingEntity> findByIdWithGraph(@NotNull UUID id);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BuildingEntity b WHERE b.name = :name AND b.enterprise.id = :enterpriseId")
    boolean existsByNameBuildingInEnterprise(@NotBlank String name, UUID enterpriseId);

}
