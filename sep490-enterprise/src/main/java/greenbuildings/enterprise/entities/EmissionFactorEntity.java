package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractBaseEntity;
import greenbuildings.enterprise.entities.enums.EmissionSource;
import greenbuildings.enterprise.entities.enums.FuelType;
import greenbuildings.enterprise.entities.enums.UnitType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "emission_factor")
@Getter
@Setter
public class EmissionFactorEntity extends AbstractBaseEntity {
    
    @Column(name = "emission_source", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmissionSource emissionSource;
    
    @Column(name = "fuel_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    
    @Column(name = "co2", nullable = false, precision = 20)
    private BigDecimal co2 = BigDecimal.ZERO;
    
    @Column(name = "ch4", nullable = false, precision = 20)
    private BigDecimal ch4 = BigDecimal.ZERO;
    
    @Column(name = "n2o", nullable = false, precision = 20)
    private BigDecimal n2o = BigDecimal.ZERO;
    
    @Column(name = "co2e", nullable = false, precision = 20)
    private BigDecimal co2e = BigDecimal.ZERO;
    
    @Column(name = "is_direct_convert", nullable = false)
    private boolean isDirectConvert;
    
    @Column(name = "input_unit", nullable = false)
    @Enumerated(EnumType.STRING)
    private UnitType inputUnit;
}
