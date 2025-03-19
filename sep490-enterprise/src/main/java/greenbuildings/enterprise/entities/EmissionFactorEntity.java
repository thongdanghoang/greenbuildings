package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import greenbuildings.enterprise.enums.EmissionUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    
    @Column(name = "name_vi")
    private String nameVN;
    
    @Column(name = "name_en")
    private String nameEN;
    
    @Column(name = "name_zh")
    private String nameZH;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unit_numerator")
    private EmissionUnit emissionUnitNumerator; // Mostly kgCO2, kgN20, kgCH4
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unit_denominator")
    private EmissionUnit emissionUnitDenominator; // Mostly TJ, there are some special cases with direct conversion
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "emission_source_id")
    private EmissionSourceEntity source;
    
    @Column(name = "description")
    private String description;
    
    @NotNull
    @Column(name = "valid_from")
    private LocalDateTime validFrom;
    
    @Column(name = "valid_to")
    private LocalDateTime validTo;
    
    @Column(name = "is_direct_emission")
    private boolean isDirectEmission;
    
    @ManyToOne(optional = true) // Maybe null in case of direct conversion
    @JoinColumn(name = "energy_conversion_id")
    private EnergyConversionEntity energyConversion;
}

// Note:
// Despite the default value of optional in @ManyToOne is true => the column may be null
// The generated SQL will be inner join
// But with explicitly define optional = true => the generated SQL will be left join