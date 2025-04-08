package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractBaseEntity;
import greenbuildings.enterprise.enums.EmissionUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chemical_density")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChemicalDensityEntity extends AbstractBaseEntity {

    @Column(name = "chemical_formula", nullable = false, unique = true)
    private String chemicalFormula;

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "unit_numerator", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmissionUnit unitNumerator;

    @Column(name = "unit_denominator", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmissionUnit unitDenominator;
} 