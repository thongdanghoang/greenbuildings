package greenbuildings.enterprise.entities;

import greenbuildings.commons.springfw.impl.entities.AbstractAuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Entity
@Table(name = "assets")
@Getter
@Setter
@FieldNameConstants
public class AssetEntity extends AbstractAuditableEntity {
    
    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    private EnterpriseEntity enterprise;
    
    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private TenantEntity tenant;
    
    @NotBlank
    @Column(name = "name")
    private String name;
    
    @NotBlank
    @Size(max = 32)
    @Column(name = "code")
    private String code;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "disabled")
    private boolean disabled;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private BuildingEntity building;
    
    public static AssetEntity of(UUID id) {
        AssetEntity asset = new AssetEntity();
        asset.setId(id);
        return asset;
    }
    
    public static AssetEntity clone(AssetEntity asset) {
        AssetEntity clone = new AssetEntity();
        clone.setName(asset.getName());
        clone.setDescription(asset.getDescription());
        clone.setBuilding(asset.getBuilding());
        clone.setTenant(asset.getTenant());
        clone.setEnterprise(asset.getEnterprise());
        return clone;
    }
}
