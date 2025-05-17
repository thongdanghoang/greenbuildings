package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.SoftDelete;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "buildings")
@SoftDelete
@Getter
@Setter
@FieldNameConstants
@NoArgsConstructor
public class BuildingEntity extends AbstractAuditableEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private EnterpriseEntity enterprise;
    
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotBlank
    @Column(name = "address", nullable = false)
    private String address;
    
    @Column(name = "number_of_levels")
    private int numberOfLevels;
    
    @Column(name = "area")
    private double area;
    
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    @Column(name = "latitude")
    private double latitude;
    
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    @Column(name = "longitude")
    private double longitude;
    
    @OneToMany(mappedBy = "building")
    private Set<SubscriptionEntity> subscriptions = new HashSet<>();
    
    @OneToMany(mappedBy = "building")
    private Set<BuildingGroupEntity> buildingGroups = new HashSet<>();
    
    @OneToMany(mappedBy = "building")
    private Set<ActivityTypeEntity> activityTypes = new HashSet<>();
    
    public static BuildingEntity of(UUID id) {
        var building = new BuildingEntity();
        building.setId(id);
        return building;
    }
}
