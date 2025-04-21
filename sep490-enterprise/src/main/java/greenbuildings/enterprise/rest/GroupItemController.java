package greenbuildings.enterprise.rest;

import commons.springfw.impl.controller.AbstractRestController;
import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.dtos.GroupItemDTO;
import greenbuildings.enterprise.dtos.NewGroupItemDTO;
import greenbuildings.enterprise.dtos.SearchGroupItemDTO;
import greenbuildings.enterprise.entities.GroupItemEntity;
import greenbuildings.enterprise.mappers.GroupItemMapper;
import greenbuildings.enterprise.services.GroupItemService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/group-items")
@RequiredArgsConstructor
@RolesAllowed({
        UserRole.RoleNameConstant.ENTERPRISE_OWNER,
        UserRole.RoleNameConstant.ENTERPRISE_EMPLOYEE
})
public class GroupItemController extends AbstractRestController {
    
    private final GroupItemService groupItemService;
    private final GroupItemMapper groupItemMapper;
    
    @PostMapping
    public ResponseEntity<GroupItemDTO> create(@RequestBody NewGroupItemDTO dto) {
        GroupItemEntity groupItem = groupItemMapper.toEntity(dto);
        return ResponseEntity.ok(groupItemMapper.toDTO(groupItemService.create(groupItem)));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GroupItemDTO> findById(@PathVariable UUID id) {
        return groupItemService.findById(id)
                .map(groupItemMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<GroupItemDTO>> findAll() {
        return ResponseEntity.ok(groupItemService.findAll().stream()
                .map(groupItemMapper::toDTO)
                .toList());
    }
    
    @GetMapping("/building-group/{buildingGroupId}")
    public ResponseEntity<List<GroupItemDTO>> findByBuildingGroupId(@PathVariable UUID buildingGroupId) {
        return ResponseEntity.ok(groupItemService.findByBuildingGroupId(buildingGroupId).stream()
                .map(groupItemMapper::toDTO)
                .toList());
    }
    
    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<GroupItemDTO>> search(@RequestBody SearchCriteriaDTO<SearchGroupItemDTO> searchCriteria) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = groupItemService.findAll(pageable);
        return ResponseEntity.ok(CommonMapper.toSearchResultDTO(searchResults, groupItemMapper::toDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<GroupItemDTO> update(@PathVariable UUID id, @RequestBody GroupItemDTO dto) {
        return ResponseEntity.ok(groupItemMapper.toDTO(groupItemService.update(id, dto)));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        groupItemService.delete(id);
        return ResponseEntity.ok().build();
    }
} 