package greenbuildings.enterprise.rest;

import commons.springfw.impl.controller.AbstractRestController;
import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.dtos.BuildingGroupCriteria;
import greenbuildings.enterprise.dtos.BuildingGroupDTO;
import greenbuildings.enterprise.dtos.CreateBuildingGroupDTO;
import greenbuildings.enterprise.dtos.InviteTenantToBuildingGroup;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import greenbuildings.enterprise.mappers.BuildingGroupMapper;
import greenbuildings.enterprise.services.BuildingGroupService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/building-groups")
@RequiredArgsConstructor
@RolesAllowed({
        UserRole.RoleNameConstant.ENTERPRISE_OWNER,
        UserRole.RoleNameConstant.ENTERPRISE_EMPLOYEE
})
public class BuildingGroupController extends AbstractRestController {
    
    private final BuildingGroupService buildingGroupService;
    private final BuildingGroupMapper buildingGroupMapper;
    
    @PostMapping
    public ResponseEntity<BuildingGroupDTO> createOrUpdate(@RequestBody CreateBuildingGroupDTO dto) {
        BuildingGroupEntity buildingGroupEntity = buildingGroupMapper.toEntity(dto);
        return ResponseEntity.ok(buildingGroupMapper.toDTO(buildingGroupService.create(buildingGroupEntity)));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BuildingGroupDTO> findById(@PathVariable UUID id) {
        return buildingGroupService.findById(id)
                .map(buildingGroupMapper::toDetailDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<BuildingGroupDTO>> findAll() {
        return ResponseEntity.ok(buildingGroupService.findAll().stream()
                .map(buildingGroupMapper::toDTO)
                .toList());
    }
    
    @GetMapping("/building/{buildingId}")
    public ResponseEntity<List<BuildingGroupDTO>> findByBuildingId(@PathVariable UUID buildingId) {
        return ResponseEntity.ok(buildingGroupService.findByBuildingId(buildingId).stream()
                .map(buildingGroupMapper::toDTO)
                .toList());
    }
    
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<BuildingGroupDTO>> findByTenantId(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(buildingGroupService.findByTenantId(tenantId).stream()
                .map(buildingGroupMapper::toDTO)
                .toList());
    }
    
    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<BuildingGroupDTO>> search(@RequestBody SearchCriteriaDTO<BuildingGroupCriteria> searchCriteria) {
        Page<BuildingGroupEntity> result = buildingGroupService.search(searchCriteria);
        return ResponseEntity.ok(CommonMapper.toSearchResultDTO(result, buildingGroupMapper::toDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BuildingGroupDTO> update(@PathVariable UUID id, @RequestBody BuildingGroupDTO dto) {
        return ResponseEntity.ok(buildingGroupMapper.toDTO(buildingGroupService.update(id, dto)));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        buildingGroupService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/building/{buildingId}/available")
    public ResponseEntity<List<BuildingGroupDTO>> getAvailableBuildingGroups(@PathVariable UUID buildingId) {
        try {
            List<BuildingGroupEntity> availableGroups = buildingGroupService.getAvailableBuildingGroups(buildingId);
            return ResponseEntity.ok(availableGroups.stream()
                    .map(buildingGroupMapper::toDTO)
                    .toList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/invite")
    public ResponseEntity<?> inviteTenant(@RequestBody InviteTenantToBuildingGroup dto) {
        
        return null;
    }
} 