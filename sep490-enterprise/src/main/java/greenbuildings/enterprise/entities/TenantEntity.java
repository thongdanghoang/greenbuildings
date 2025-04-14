package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tenant")
public class TenantEntity extends AbstractAuditableEntity {

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "tenant")
    private Set<BuildingGroupEntity> buildingGroups = new HashSet<>();
    
    @OneToMany(mappedBy = "tenant")
    private Set<ActivityTypeEntity> activityTypes = new HashSet<>();


}
