package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.commons.api.views.DateRangeView;
import greenbuildings.commons.api.views.SelectableItem;
import greenbuildings.commons.springfw.impl.UserLanguage;
import greenbuildings.commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.springfw.impl.securities.UserContextData;
import greenbuildings.enterprise.dtos.CreateEmissionActivityDTO;
import greenbuildings.enterprise.dtos.EmissionActivityCriteria;
import greenbuildings.enterprise.dtos.EmissionActivityDTO;
import greenbuildings.enterprise.dtos.EmissionActivityDetailsDTO;
import greenbuildings.enterprise.dtos.EmissionActivityTableView;
import greenbuildings.enterprise.dtos.emission_activities.ActivityCriteria;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.mappers.EmissionActivityMapper;
import greenbuildings.enterprise.services.EmissionActivityService;
import greenbuildings.enterprise.views.emission_activities.EmissionActivityView;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(EmissionActivityController.PATH)
@RequiredArgsConstructor
@Slf4j
public class EmissionActivityController {
    
    public static final String PATH = "emission-activity";
    
    private final EmissionActivityService emissionActivityService;
    private final EmissionActivityMapper mapper;
    
    @GetMapping("/{id}")
    public ResponseEntity<EmissionActivityDetailsDTO> getEmissionActivityDetails(@PathVariable UUID id) {
        EmissionActivityEntity entity = emissionActivityService.getEmissionActivityDetails(id);
        return ResponseEntity.ok(mapper.toDetailsDTO(entity));
    }
    
    @GetMapping("/{id}/recorded-date-ranges")
    public ResponseEntity<List<DateRangeView>> getRecordedDateRanges(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID excludeRecordId,
            @RequestParam(required = false) UUID assetId) {
        return ResponseEntity.ok(mapper.toDateRangeView(emissionActivityService.findRecordedDateRangesById(id, excludeRecordId, assetId)));
    }
    
    @PostMapping("/building")
    public ResponseEntity<SearchResultDTO<EmissionActivityTableView>> findAllByCriteria(@RequestBody SearchCriteriaDTO<EmissionActivityCriteria> criteria) {
        Page<EmissionActivityEntity> list = emissionActivityService.search(criteria);
        return ResponseEntity.ok(CommonMapper.toSearchResultDTO(list, mapper::toTableView));
    }
    
    @PostMapping("/search")
    @RolesAllowed(UserRole.RoleNameConstant.ENTERPRISE_OWNER)
    public ResponseEntity<SearchResultDTO<EmissionActivityView>> search(@RequestBody SearchCriteriaDTO<ActivityCriteria> searchCriteria,
                                                                        @AuthenticationPrincipal UserContextData userContext) {
        var enterpriseId = userContext.getEnterpriseId().orElseThrow();
        var pageable = CommonMapper.toPageableFallbackSortToLastModifiedDate(searchCriteria.page(), searchCriteria.sort());
        var searchResults = emissionActivityService.search(pageable, enterpriseId, searchCriteria.criteria());
        return ResponseEntity.ok(CommonMapper.toSearchResultDTO(searchResults, this.mapper::toEmissionActivityView));
    }
    
    @GetMapping("/selectable-buildings")
    @RolesAllowed({UserRole.RoleNameConstant.ENTERPRISE_OWNER, UserRole.RoleNameConstant.TENANT})
    public ResponseEntity<List<SelectableItem<UUID>>> getSelectableBuildings() {
        var buildings = emissionActivityService
                .getBuildingsByEnterpriseId()
                .stream()
                .map(building -> new SelectableItem<>(building.getName(), building.getId(), false))
                .toList();
        return ResponseEntity.ok(buildings);
    }
    
    @GetMapping("/selectable-factors")
    @RolesAllowed(UserRole.RoleNameConstant.ENTERPRISE_OWNER)
    public ResponseEntity<List<SelectableItem<UUID>>> getEmissionFactors(
            @RequestParam(defaultValue = "vi") String language,
            @AuthenticationPrincipal UserContextData userContext) {
        var enterpriseId = userContext.getEnterpriseId().orElseThrow();
        var buildings = emissionActivityService
                .getEmissionFactorsByEnterpriseId(enterpriseId)
                .stream()
                .map(factor -> new SelectableItem<>(
                        UserLanguage.fromCode(language).getByLanguage(factor.getNameVN(), factor.getNameEN(), factor.getNameZH()),
                        factor.getId(),
                        false)
                    )
                .toList();
        return ResponseEntity.ok(buildings);
    }
    
    @PostMapping
    public ResponseEntity<EmissionActivityDetailsDTO> addOrUpdateEmissionActivity(@RequestBody CreateEmissionActivityDTO dto) {
        EmissionActivityEntity entity;
        if (dto.id() != null) {
            entity = this.mapper.updateActivity(dto);
        } else {
            entity = this.mapper.createNewActivity(dto);
        }
        return ResponseEntity.ok(mapper.toDetailsDTO(emissionActivityService.addOrUpdate(entity)));
    }
    
    @DeleteMapping
    public ResponseEntity<Void> deleteActivities(@RequestBody Set<UUID> ids) {
        emissionActivityService.deleteActivities(ids);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/building/{id}")
    public ResponseEntity<List<EmissionActivityDTO>> getAllActivitiesByBuildingId(@PathVariable UUID id) {
        List<EmissionActivityEntity> list = emissionActivityService.getAllActivitiesByBuildingId(id);
        return ResponseEntity.ok(list.stream().map(mapper::toDTO).toList());
    }
}
