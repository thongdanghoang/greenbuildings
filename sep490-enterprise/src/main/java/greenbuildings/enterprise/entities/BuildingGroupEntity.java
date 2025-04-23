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
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "building_group")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class BuildingGroupEntity extends AbstractAuditableEntity {
    
    @Column(name = "building_group_name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @NotNull
    @NonNull
    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    private BuildingEntity building;
    
    @ManyToOne()
    @JoinColumn(name = "tenant_id")
    private TenantEntity tenant;
    
    @OneToMany(mappedBy = "buildingGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<EmissionActivityEntity> emissionActivities = new HashSet<>();
    
    @OneToMany(mappedBy = "buildingGroup")
    private Set<GroupItemEntity> groupItems = new HashSet<>();
    
    @OneToMany(mappedBy = "buildingGroup")
    private Set<InvitationEntity> invitations = new HashSet<>();
}
