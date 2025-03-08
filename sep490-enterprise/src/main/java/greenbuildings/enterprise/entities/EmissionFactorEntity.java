package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import greenbuildings.enterprise.enums.EmissionUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "emission_factor")
@Getter
@Setter
@NoArgsConstructor
public class EmissionFactorEntity extends AbstractAuditableEntity {
    
    @Column(name = "co2")
    private BigDecimal co2 = BigDecimal.ZERO;
    
    @Column(name = "ch4")
    private BigDecimal ch4 = BigDecimal.ZERO;
    
    @Column(name = "n2o")
    private BigDecimal n2o = BigDecimal.ZERO;
    
    @Column(name = "name")
    private String name;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unit_numerator")
    private EmissionUnit emissionUnitNumerator;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unit_denominator")
    private EmissionUnit emissionUnitDenominator;
    
    @Column(name = "source")
    private String source;
    
    @Column(name = "description")
    private String description;
    
    @NotNull
    @Column(name = "valid_from")
    private LocalDateTime validFrom;
    
    @Column(name = "valid_to")
    private LocalDateTime validTo;
    
    @Column(name = "is_direct_emission")
    private boolean isDirectEmission;
    
    @Column(name = "conversion_value")
    private BigDecimal conversionValue;
    
    @Column(name = "conversion_unit_numerator")
    @Enumerated(EnumType.STRING)
    private EmissionUnit conversionUnitNumerator;
    
    
    @Column(name = "conversion_unit_denominator")
    @Enumerated(EnumType.STRING)
    private EmissionUnit conversionUnitDenominator;
}
