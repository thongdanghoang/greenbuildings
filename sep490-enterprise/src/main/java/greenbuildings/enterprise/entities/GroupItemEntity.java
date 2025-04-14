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
@Table(name = "group_items")
public class GroupItemEntity extends AbstractAuditableEntity {
    
    @Column(name = "item_name")
    private String itemName;
    
    @ManyToOne
    @JoinColumn(name = "building_group_id", nullable = false)
    private BuildingGroupEntity buildingGroup;
    
    @OneToMany(mappedBy = "groupItem", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private Set<EmissionActivityRecordEntity> records = new HashSet<>();
    
    
}
