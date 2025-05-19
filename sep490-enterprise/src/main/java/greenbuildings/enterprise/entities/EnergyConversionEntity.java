package greenbuildings.enterprise.entities;

import greenbuildings.commons.springfw.impl.entities.AbstractBaseEntity;
import greenbuildings.enterprise.enums.EmissionUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "energy_conversion")
@Getter
@Setter
public class EnergyConversionEntity extends AbstractBaseEntity {
    
    @OneToOne
    @JoinColumn(name = "fuel_id")
    private FuelEntity fuel;
    
    @Column(name = "conversion_value")
    private BigDecimal conversionValue;
    
    @Column(name = "conversion_unit_numerator")
    @Enumerated(EnumType.STRING)
    private EmissionUnit conversionUnitNumerator;
    
    @Column(name = "conversion_unit_denominator")
    @Enumerated(EnumType.STRING)
    private EmissionUnit conversionUnitDenominator;
    
}
