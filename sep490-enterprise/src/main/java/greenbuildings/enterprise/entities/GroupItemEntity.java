package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "group_items")
public class GroupItemEntity extends AbstractAuditableEntity {
    
    private String itemName;
    
    @OneToMany(mappedBy = "groupItem", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private Set<EmissionActivityRecordEntity> records = new HashSet<>();
    
    
}
