package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "emission_activity")
@Getter
@Setter
public class EmissionActivityEntity extends AbstractBaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    private BuildingEntity building;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false, precision = 20)
    private BigDecimal data; // unit must be same of emission factor
    
    @ManyToOne
    @JoinColumn(name = "emission_factor_id", nullable = false)
    private EmissionFactorEntity emissionFactor;
    
}
