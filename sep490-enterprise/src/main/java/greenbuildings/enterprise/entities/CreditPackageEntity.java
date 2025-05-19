package greenbuildings.enterprise.entities;

import greenbuildings.commons.springfw.impl.entities.AbstractAuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "credit_packages")
@Getter
@Setter
@NoArgsConstructor
public class CreditPackageEntity extends AbstractAuditableEntity {
    @Column(name = "active")
    private boolean active;
}
