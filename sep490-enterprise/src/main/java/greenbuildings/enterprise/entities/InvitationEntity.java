package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import greenbuildings.enterprise.enums.InvitationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invitations")
@Getter
@Setter
public class InvitationEntity extends AbstractAuditableEntity {
    
    @ManyToOne
    @JoinColumn(name = "building_group_id", nullable = false)
    private BuildingGroupEntity buildingGroup;
    
    @Column(name = "email", nullable = false)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InvitationStatus status;
}
