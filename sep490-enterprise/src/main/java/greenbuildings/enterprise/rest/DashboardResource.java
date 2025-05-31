package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.security.UserRole;
import greenbuildings.commons.springfw.impl.securities.UserContextData;
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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/dashboards")
@RequiredArgsConstructor
@RolesAllowed({UserRole.RoleNameConstant.ENTERPRISE_OWNER})
public class DashboardResource {
    
    private final EnterpriseService enterpriseService;
    private final DashboardService dashboardService;
    private final EnterpriseDashboardMapper dashboardMapper;
    private final EmissionActivityService emissionActivityService;
    private final TaskExecutor taskExecutor;
    
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
        var activities = emissionActivityService.findByEnterpriseIdFetchRecords(enterpriseId);
        var entities = emissionActivityService.calculationActivitiesTotalGhg(activities);
        
        // Fire off concurrent tasks
        var sourcesF = CompletableFuture
                .supplyAsync(() -> emissionActivityService
                        .getTopEmissionSourcesWithHighestEmissions(entities, 5), taskExecutor)
                .thenApply(map -> map.entrySet().stream()
                                     .map(e -> new DistributionEmissionSource(e.getKey().getNameVN(), e.getValue().setScale(0, RoundingMode.DOWN)))
                                     .toList());
        
        var buildingsF = CompletableFuture
                .supplyAsync(() -> emissionActivityService
                        .getTopBuildingsWithHighestEmissions(entities, 5), taskExecutor)
                .thenApply(map -> map.entrySet().stream()
                                     .map(e -> new BuildingGhgEmission(e.getKey().getName(), e.getValue().setScale(0, RoundingMode.DOWN)))
                                     .toList());
        
        var totalF = CompletableFuture
                .supplyAsync(() -> emissionActivityService.calculateTotalEmissions(entities).setScale(0, RoundingMode.DOWN), taskExecutor);
        
        // Wait for all to finish
        CompletableFuture.allOf(sourcesF, buildingsF, totalF).join();
        
        // Build the response
        DefaultChartView view = DefaultChartView
                .builder()
                .distributionEmissionSources(sourcesF.join())
                .buildingGhgEmissions(buildingsF.join())
                .totalEnterpriseEmissions(totalF.join())
                .build();
        
        return ResponseEntity.ok(view);
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
