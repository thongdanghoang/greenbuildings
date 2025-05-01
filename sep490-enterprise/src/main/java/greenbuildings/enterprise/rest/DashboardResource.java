package greenbuildings.enterprise.rest;

import commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.dtos.dashboard.EnterpriseDashboardDTO;
import greenbuildings.enterprise.mappers.EnterpriseDashboardMapper;
import greenbuildings.enterprise.services.DashboardService;
import greenbuildings.enterprise.services.EnterpriseService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboards")
@RequiredArgsConstructor
@RolesAllowed({UserRole.RoleNameConstant.ENTERPRISE_OWNER})
public class DashboardResource {
    
    private final EnterpriseService enterpriseService;
    private final DashboardService dashboardService;
    private final EnterpriseDashboardMapper dashboardMapper;
    
    @GetMapping
    public ResponseEntity<List<EnterpriseDashboardDTO>> getEnterpriseDashboards(@AuthenticationPrincipal UserContextData userContextData) {
        var enterpriseDashboards = dashboardService
                .getEnterpriseDashboards(userContextData.getEnterpriseId().orElseThrow()).stream()
                .map(dashboardMapper::toEnterpriseDashboardDTO)
                .toList();
        return ResponseEntity.ok(enterpriseDashboards);
    }
    
    @PostMapping
    public ResponseEntity<EnterpriseDashboardDTO> createEnterpriseDashboard(
            @AuthenticationPrincipal UserContextData userContextData,
            @RequestBody EnterpriseDashboardDTO dashboardDTO) {
        var enterprise = enterpriseService.getById(userContextData.getEnterpriseId().orElseThrow());
        var dashboardEntity = dashboardMapper.createEnterpriseDashboardEntity(dashboardDTO, enterprise);
        var createdDashboard = dashboardService.createEnterpriseDashboard(dashboardEntity);
        return ResponseEntity.ok(dashboardMapper.toEnterpriseDashboardDTO(createdDashboard));
    }
    
}
