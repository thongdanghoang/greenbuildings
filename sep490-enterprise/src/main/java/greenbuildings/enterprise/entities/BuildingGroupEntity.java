package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "building_group")
public class BuildingGroupEntity extends AbstractAuditableEntity {
    
    @Column(name = "building_group_name")
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    private BuildingEntity building;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "tenant_id", nullable = true)
    private TenantEntity tenant;
    
    @OneToMany(mappedBy = "buildingGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<EmissionActivityEntity> emissionActivities = new HashSet<>();
    
}
