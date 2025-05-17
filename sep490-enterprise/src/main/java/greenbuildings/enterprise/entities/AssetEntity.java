package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.SoftDelete;

import java.util.UUID;

@Entity
@SoftDelete
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
    
    @Column(name = "description")
    private String description;
    
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
