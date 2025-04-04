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

    Page<BuildingEntity> findByEnterpriseIdAndDeleted(UUID enterpriseId, boolean deleted, Pageable pageable);
    
    List<BuildingEntity> findAllByEnterpriseId(UUID enterpriseId);
    
    Optional<BuildingEntity> findByIdAndEnterpriseId(UUID id, UUID enterpriseId);
    
    @EntityGraph(BuildingEntity.WITH_ACTIVITIES_ENTITY_GRAPH)
    @Query(value = "SELECT b FROM BuildingEntity b where b.id=:id")
    Optional<BuildingEntity> findByIdWithGraph(@NotNull UUID id);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BuildingEntity b WHERE b.name = :name AND b.enterprise.id = :enterpriseId AND b.deleted = false")
    boolean existsByNameBuildingInEnterprise(@NotBlank String name, UUID enterpriseId);

}
