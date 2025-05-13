package greenbuildings.enterprise.rest;

import commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.dtos.dashboard.EnterpriseDashboardDTO;
import greenbuildings.enterprise.mappers.EnterpriseDashboardMapper;
import greenbuildings.enterprise.services.DashboardService;
import greenbuildings.enterprise.services.EmissionActivityService;
import greenbuildings.enterprise.services.EnterpriseService;
import greenbuildings.enterprise.views.dashboard.BuildingGhgEmission;
import greenbuildings.enterprise.views.dashboard.DefaultChartView;
import greenbuildings.enterprise.views.dashboard.DistributionEmissionSource;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/dashboards")
@RequiredArgsConstructor
@RolesAllowed({UserRole.RoleNameConstant.ENTERPRISE_OWNER})
public class DashboardResource {
    
    private final EnterpriseService enterpriseService;
    private final DashboardService dashboardService;
    private final EnterpriseDashboardMapper dashboardMapper;
    private final EmissionActivityService emissionActivityService;
    
    @GetMapping
    public ResponseEntity<List<EnterpriseDashboardDTO>> getEnterpriseDashboards(@AuthenticationPrincipal UserContextData userContextData) {
        var enterpriseDashboards = dashboardService
                .getEnterpriseDashboards(userContextData.getEnterpriseId().orElseThrow()).stream()
                .map(dashboardMapper::toEnterpriseDashboardDTO)
                .toList();
        return ResponseEntity.ok(enterpriseDashboards);
    }
    
    @GetMapping("/default")
    public ResponseEntity<DefaultChartView> getDefault(@AuthenticationPrincipal UserContextData userContextData) {
        var enterpriseId = userContextData.getEnterpriseId().orElseThrow();
        var distributionEmissionSources = emissionActivityService
                .getTopEmissionSourcesWithHighestEmissions(enterpriseId, 5)
                .entrySet().stream()
                .map(entry -> new DistributionEmissionSource(entry.getKey().getNameVN(), entry.getValue().setScale(0, RoundingMode.DOWN)))
                .toList();
        var buildingGhgEmissions = emissionActivityService
                .getTopBuildingsWithHighestEmissions(enterpriseId, 5)
                .entrySet().stream()
                .map(entry -> new BuildingGhgEmission(entry.getKey().getName(), entry.getValue().setScale(0, RoundingMode.DOWN)))
                .toList();
        var totalEnterpriseEmissions = emissionActivityService.calculateTotalEmissions(enterpriseId).setScale(0, RoundingMode.DOWN);
        return ResponseEntity.ok(
                DefaultChartView.builder()
                                .totalEnterpriseEmissions(totalEnterpriseEmissions)
                                .buildingGhgEmissions(buildingGhgEmissions)
                                .distributionEmissionSources(distributionEmissionSources)
                                .build()
                                );
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
