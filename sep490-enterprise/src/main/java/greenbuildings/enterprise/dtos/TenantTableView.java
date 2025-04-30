package greenbuildings.enterprise.dtos;

import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import greenbuildings.enterprise.entities.TenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantTableView {
    private UUID id;
    private String name;
    private String email;
    private String hotline;
    private List<BuildingView> buildings;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BuildingView {
        private UUID id;
        private String name;
        private BuildingGroupView buildingGroup;
        
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class BuildingGroupView {
            private UUID id;
            private String name;
        }
    }
    
    public static TenantTableView fromEntity(TenantEntity tenant) {
        List<BuildingView> buildingViews = tenant
                .getBuildingGroups()
                .stream()
                .map(BuildingGroupEntity::getBuilding)
                .filter(x -> x.getEnterprise().getId().equals(SecurityUtils.getCurrentUserEnterpriseId().orElseThrow()))
                .map(x -> {
                    BuildingGroupEntity buildingGroupEntity = x.getBuildingGroups()
                                                               .stream()
                                                               .filter(y -> y.getTenant() != null)
                                                               .filter(y -> y.getTenant().getId().equals(tenant.getId()))
                                                               .findFirst()
                                                               .orElseThrow();
                    
                    BuildingView.BuildingGroupView buildingGroupView = BuildingView.BuildingGroupView.builder()
                                                                                                     .id(buildingGroupEntity.getId())
                                                                                                     .name(buildingGroupEntity.getName())
                                                                                                     .build();
                    
                    return BuildingView.builder()
                                       .id(x.getId())
                                       .name(x.getName())
                                       .buildingGroup(buildingGroupView)
                                       .build();
                })
                .toList();
        
        return TenantTableView.builder()
                              .id(tenant.getId())
                              .name(tenant.getName())
                              .email(tenant.getEmail())
                              .hotline(tenant.getHotline())
                              .buildings(buildingViews)
                              .build();
    }
}
