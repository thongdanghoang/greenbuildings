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
@Table(name = "machine_fuel_emission")
@Getter
@Setter
public class MachineFuelEmissionEntity extends AbstractBaseEntity {
    
    @Column(nullable = false, precision = 20)
    private BigDecimal data; // amount of consumption - ex: 100kg Coal, 200KwH, 300m^3 diesel
    
    @ManyToOne
    @JoinColumn(name = "machine_id", nullable = false)
    private MachineEntity machine;
    
    @ManyToOne
    @JoinColumn(name = "emission_factor_id", nullable = false)
    private EmissionFactorEntity emissionFactor; // maybe diesel or kwh
    
}
