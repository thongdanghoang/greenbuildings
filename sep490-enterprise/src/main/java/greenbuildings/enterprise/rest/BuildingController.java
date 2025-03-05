package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.dtos.BuildingDTO;
import greenbuildings.enterprise.mappers.BuildingMapper;
import greenbuildings.enterprise.services.BuildingService;
import greenbuildings.enterprise.services.EnterpriseService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/buildings")
@RequiredArgsConstructor
@RolesAllowed({
        UserRole.RoleNameConstant.ENTERPRISE_OWNER,
        UserRole.RoleNameConstant.ENTERPRISE_EMPLOYEE
})
public class BuildingController {
    
    private final BuildingMapper buildingMapper;
    private final BuildingService buildingService;
    private final EnterpriseService enterpriseService;
    
    @GetMapping("/{id}")
    public ResponseEntity<BuildingDTO> getBuildingById(@PathVariable UUID id) {
        var building = buildingService.findById(id);
        return ResponseEntity.ok(buildingMapper.toDto(building.orElseThrow()));
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
    
    @PostMapping("/machine")
    public void addMachine() {
        // TODO
    }
    
    @PostMapping("/activity")
    public void addEmissionActivity() {
        // TODO
    }
    
}
