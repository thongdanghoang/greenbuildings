package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractBaseEntity;
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
@Table(name = "energy_conversion")
@Getter
@Setter
public class EnergyConversionEntity extends AbstractBaseEntity {
    
    @Column(name = "fuel_type", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    
    @Column(name = "joules_from")
    private BigDecimal joulesFrom; // Unit: Tera Joules
    
    @Column(name = "joules_to")
    private BigDecimal joulesTo; // Unit: Tera Joules
    
    @Column(name = "is_value_range")
    private boolean isValueRange;
    
    @Column(name = "base_unit")
    @Enumerated(EnumType.STRING)
    private UnitType baseUnit; // maybe base on liquid: m^3 or mass: giga gram, ton (mega gram), kilogram
    
}
