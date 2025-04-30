package greenbuildings.enterprise.rest;

import commons.springfw.impl.controller.AbstractRestController;
import commons.springfw.impl.mappers.CommonMapper;
import commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.dtos.BuildingDTO;
import greenbuildings.enterprise.dtos.DownloadReportDTO;
import greenbuildings.enterprise.dtos.OverviewBuildingDTO;
import greenbuildings.enterprise.dtos.dashboard.SelectableBuildingDTO;
import greenbuildings.enterprise.mappers.BuildingGroupMapper;
import greenbuildings.enterprise.mappers.BuildingMapper;
import greenbuildings.enterprise.services.BuildingGroupService;
import greenbuildings.enterprise.services.BuildingService;
import greenbuildings.enterprise.services.EnterpriseService;
import greenbuildings.enterprise.views.buildings.details.TenantView;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/buildings")
@RequiredArgsConstructor
@RolesAllowed({
        UserRole.RoleNameConstant.ENTERPRISE_OWNER
})
public class BuildingController extends AbstractRestController {
    
    private final BuildingMapper buildingMapper;
    private final BuildingService buildingService;
    private final EnterpriseService enterpriseService;
    private final BuildingGroupService buildingGroupService;
    private final BuildingGroupMapper buildingGroupMapper;
    
    @GetMapping("/{id}")
    public ResponseEntity<BuildingDTO> getBuildingById(@PathVariable UUID id) {
        var building = buildingService.findById(id);
        return ResponseEntity.ok(buildingMapper.toDto(building.orElseThrow()));
    }
    
    @GetMapping("/selectable")
    public ResponseEntity<List<SelectableBuildingDTO>> getAllBuildings(@AuthenticationPrincipal UserContextData userContextData) {
        var enterpriseIdFromContext = Objects.requireNonNull(userContextData.getEnterpriseId());
        var buildings = buildingService.findBuildingsByEnterpriseId(enterpriseIdFromContext);
        return ResponseEntity.ok(buildings.stream().map(buildingMapper::toSelectableBuildingDTO).toList());
    }
    
    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<BuildingDTO>> searchEnterpriseBuildings(@RequestBody SearchCriteriaDTO<Void> searchCriteria,
                                                                                  @AuthenticationPrincipal UserContextData userContextData) {
        var enterpriseIdFromContext = Objects.requireNonNull(userContextData.getEnterpriseId());
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = buildingService.getEnterpriseBuildings(enterpriseIdFromContext, pageable);
        var searchResultDTO = CommonMapper.toSearchResultDTO(searchResults, buildingMapper::toDto);
        return ResponseEntity.ok(searchResultDTO);
    }
    
    @PostMapping("/{buildingId}/tenants/search")
    public ResponseEntity<SearchResultDTO<TenantView>> searchUserByBuildings(@RequestBody SearchCriteriaDTO<Void> searchCriteria,
                                                                             @PathVariable UUID buildingId) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = buildingGroupService.searchByBuildingIdWithTenant(buildingId, pageable);
        var searchResultDTO = CommonMapper.toSearchResultDTO(
                searchResults,
                buildingGroupMapper::toTenantView);
        return ResponseEntity.ok(searchResultDTO);
    }
    
    @PostMapping
    @RolesAllowed(UserRole.RoleNameConstant.ENTERPRISE_OWNER)
    public ResponseEntity<BuildingDTO> createBuilding(@RequestBody BuildingDTO buildingDTO,
                                                      @AuthenticationPrincipal UserContextData userContextData) {
        var enterpriseIdFromContext = Objects.requireNonNull(userContextData.getEnterpriseId());
        var enterprise = enterpriseService.getById(enterpriseIdFromContext);
        var building = buildingMapper.toEntity(buildingDTO);
        building.setEnterprise(enterprise);
        var createdBuilding = buildingService.createBuilding(building);
        return ResponseEntity.status(HttpStatus.CREATED).body(buildingMapper.toDto(createdBuilding));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable UUID id) {
        buildingService.deleteBuilding(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/generate-report")
    public ResponseEntity<ByteArrayResource> generateReport(@RequestBody DownloadReportDTO downloadReport) {
        byte[] result = buildingService.generateReport(downloadReport);
        return generateFileDownloadResponse(new ByteArrayResource(result));
    }

    @GetMapping("/{id}/overview")
    public ResponseEntity<OverviewBuildingDTO> getOverviewBuildingById(@PathVariable UUID id) {
        var overviewBuilding = buildingService.getOverviewBuildingById(id);
        return ResponseEntity.ok(overviewBuilding);
    }
    
}
