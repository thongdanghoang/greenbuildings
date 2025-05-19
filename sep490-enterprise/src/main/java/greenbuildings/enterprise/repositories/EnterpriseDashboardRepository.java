package greenbuildings.enterprise.repositories;

import greenbuildings.commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.EnterpriseDashboardEntity;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public interface EnterpriseDashboardRepository extends AbstractBaseRepository<EnterpriseDashboardEntity> {
    List<EnterpriseDashboardEntity> findByEnterprise_Id(UUID enterpriseId);
    
    boolean existsByTitle(@NotBlank String title);
}
