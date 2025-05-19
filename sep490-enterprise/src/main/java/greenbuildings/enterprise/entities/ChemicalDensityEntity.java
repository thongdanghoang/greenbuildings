package greenbuildings.enterprise.entities;

import greenbuildings.commons.springfw.impl.entities.AbstractBaseEntity;
import greenbuildings.enterprise.enums.EmissionUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chemical_density")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class ChemicalDensityEntity extends AbstractBaseEntity {
    
    @NonNull
    @NotBlank
    @Column(name = "chemical_formula", nullable = false, unique = true)
    private String chemicalFormula;
    
    @Positive
    @Column(name = "value", nullable = false)
    private double value;
    
    @NonNull
    @NotNull
    @Column(name = "unit_numerator", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmissionUnit unitNumerator;
    
    @NonNull
    @NotNull
    @Column(name = "unit_denominator", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmissionUnit unitDenominator;
} 