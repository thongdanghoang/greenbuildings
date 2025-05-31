package greenbuildings.enterprise.repositories.specifications;

import greenbuildings.commons.springfw.impl.entities.AbstractBaseEntity;
import greenbuildings.enterprise.entities.AssetEntity;
import greenbuildings.enterprise.entities.BuildingEntity;

import jakarta.persistence.criteria.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AssetsSpecifications {
    
    private static final String LIKE = "%";
    
    
    private static final String id = AbstractBaseEntity.Fields.id;
    
    private static final String building = AssetEntity.Fields.building;
    private static final String name = AssetEntity.Fields.name;
    private static final String enterprise = AssetEntity.Fields.enterprise;
    private static final String tenant = AssetEntity.Fields.tenant;
    private static final String description = AssetEntity.Fields.description;
    
    private static final String buildingName = BuildingEntity.Fields.name;
    
    public static Specification<AssetEntity> withFilters(UUID organizationId, String keyword, List<UUID> buildings) {
        return (assets, query, cb) -> {
            final var predicates = new ArrayList<Predicate>();
            
            predicates.add(cb.equal(assets.get(AssetEntity.Fields.disabled), false));
            
            final var tenantId = assets.get(tenant).get(id);
            final var assetBelongToAnyTenant = cb.isNotNull(tenantId);
            final var assetBelongToCurrentTenant = cb.equal(tenantId, organizationId);
            final var assetBelongToTenant = cb.and(assetBelongToAnyTenant, assetBelongToCurrentTenant);
            
            var enterpriseId = assets.get(enterprise).get(id);
            final var assetBelongToAnyEnterprise = cb.isNotNull(enterpriseId);
            final var assetBelongToCurrentEnterprise = cb.equal(enterpriseId, organizationId);
            final var assetBelongToEnterprise = cb.and(assetBelongToAnyEnterprise, assetBelongToCurrentEnterprise);
            
            predicates.add(cb.or(assetBelongToTenant, assetBelongToEnterprise));
            
            if (StringUtils.isNotBlank(keyword)) {
                final var searchPattern = LIKE + keyword.toLowerCase() + LIKE;
                
                final var assetNameContainKeyword = cb.like(cb.lower(assets.get(name)), searchPattern);
                
                final var assetDescriptionContainKeyword = cb.like(cb.lower(assets.get(description)), searchPattern);
                
                final var buildingNameContainKeyword = cb.like(cb.lower(assets.get(building).get(buildingName)), searchPattern);
                final var assetBuildingNameContainKeyword = cb.and(cb.isNotNull(assets.get(building)), buildingNameContainKeyword);
                
                var keywordPredicate = cb
                        .or(assetNameContainKeyword, assetDescriptionContainKeyword, assetBuildingNameContainKeyword);
                predicates.add(keywordPredicate);
            }
            
            if (CollectionUtils.isNotEmpty(buildings)) {
                predicates.add(assets.get(building).get(id).in(buildings));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
