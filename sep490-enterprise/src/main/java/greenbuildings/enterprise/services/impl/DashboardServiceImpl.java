package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.entities.EnterpriseDashboardEntity;
import greenbuildings.enterprise.exceptions.EnterpriseDashboardTitleAlreadyExists;
import greenbuildings.enterprise.repositories.EnterpriseDashboardRepository;
import greenbuildings.enterprise.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class DashboardServiceImpl implements DashboardService {
    
    private final EnterpriseDashboardRepository enterpriseDashboardRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<EnterpriseDashboardEntity> getEnterpriseDashboards(UUID enterpriseId) {
        return enterpriseDashboardRepository.findByEnterprise_Id(enterpriseId);
    }
    
    @Override
    public EnterpriseDashboardEntity createEnterpriseDashboard(EnterpriseDashboardEntity dashboardEntity) {
        if(enterpriseDashboardRepository.existsByTitle(dashboardEntity.getTitle())) {
            throw new EnterpriseDashboardTitleAlreadyExists();
        }
        return enterpriseDashboardRepository.save(dashboardEntity);
    }
}
