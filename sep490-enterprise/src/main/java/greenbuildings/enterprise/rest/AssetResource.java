package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.commons.api.views.SelectableItem;
import greenbuildings.enterprise.dtos.assets.AssetDTO;
import greenbuildings.enterprise.entities.AssetEntity;
import greenbuildings.enterprise.entities.EnterpriseEntity;
import greenbuildings.enterprise.entities.TenantEntity;
import greenbuildings.enterprise.mappers.AssetMapper;
import greenbuildings.enterprise.services.AssetService;
import greenbuildings.enterprise.views.assets.AssetView;

import commons.springfw.impl.mappers.CommonMapper;
import commons.springfw.impl.securities.UserContextData;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
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
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(AssetResource.PATH)
@RolesAllowed({UserRole.RoleNameConstant.ENTERPRISE_OWNER, UserRole.RoleNameConstant.TENANT})
@RequiredArgsConstructor
public class AssetResource {
    
    public static final String PATH = "/assets";
    
    private final AssetService assetService;
    private final AssetMapper assetMapper;
    
    @PostMapping
    public ResponseEntity<AssetView> saveAsset(@RequestBody AssetDTO assetDTO,
                                               @AuthenticationPrincipal UserContextData userContextData) {
        if (Objects.nonNull(assetDTO.id())) {
            var target = assetService.getById(assetDTO.id());
            assetMapper.fullUpdate(assetMapper.toEntity(assetDTO), target);
            assignOrganizationToEntity(target, userContextData);
            return ResponseEntity.ok(assetMapper.toView(assetService.updateAsset(target)));
        }
        var target = assetMapper.toEntity(assetDTO);
        assignOrganizationToEntity(target, userContextData);
        var assetEntity = assetService.createAsset(target);
        return ResponseEntity.ok(assetMapper.toView(assetEntity));
    }
    
    private void assignOrganizationToEntity(AssetEntity entity, UserContextData userContextData) {
        boolean assigned = userContextData
                .getTenantId()
                .map(TenantEntity::of)
                .map(tenant -> {
                    entity.setTenant(tenant);
                    return true;
                })
                .orElseGet(() -> userContextData
                                   .getEnterpriseId()
                                   .map(EnterpriseEntity::of)
                                   .map(enterprise -> {
                                       entity.setEnterprise(enterprise);
                                       return true;
                                   }).orElse(false)
                          );
        if (!assigned) {
            throw new BusinessException("UserContextData must have either TenantId or EnterpriseId");
        }
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<AssetView> getAssetById(@PathVariable UUID id) {
        return ResponseEntity.ok(assetMapper.toView(assetService.getById(id)));
    }
    
    @GetMapping("/selectable")
    public ResponseEntity<List<SelectableItem<UUID>>> selectable(
            @AuthenticationPrincipal UserContextData userContext,
            @RequestParam(required = false) UUID buildingId) {
        var organizationId = userContext
                .getTenantId()
                .or(userContext::getEnterpriseId)
                .orElseThrow();
        var selectableItems = assetService
                .selectableByOrganizationId(organizationId, buildingId)
                .stream()
                .map(asset -> new SelectableItem<>(asset.getName(), asset.getId(), false))
                .toList();
        return ResponseEntity.ok(selectableItems);
    }
    
    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<AssetView>> searchAssets(
            @RequestBody SearchCriteriaDTO<Void> searchCriteria, // add criteria later by replace Void
            @AuthenticationPrincipal UserContextData userContext) {
        var organizationId = userContext
                .getTenantId()
                .or(userContext::getEnterpriseId)
                .orElseThrow();
        var pageable = CommonMapper.toPageableFallbackSortToLastModifiedDate(searchCriteria.page(), searchCriteria.sort());
        var searchResult = assetService.search(pageable, organizationId);
        return ResponseEntity.ok(CommonMapper.toSearchResultDTO(searchResult, assetMapper::toView));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable UUID id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }
    
}
