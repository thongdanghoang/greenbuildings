package greenbuildings.enterprise.services;

import greenbuildings.enterprise.entities.EnterpriseDashboardEntity;

import java.util.List;
import java.util.UUID;

public interface DashboardService {
    
    List<EnterpriseDashboardEntity> getEnterpriseDashboards(UUID enterpriseId);
    
    EnterpriseDashboardEntity createEnterpriseDashboard(EnterpriseDashboardEntity dashboardEntity);
}
