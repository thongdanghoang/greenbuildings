package greenbuildings.enterprise.dtos;

import greenbuildings.enterprise.enums.EmissionUnit;

import java.util.UUID;

public record ChemicalDensityDTO(
        UUID id,
        int version,
        String chemicalFormula,
        Double value,
        EmissionUnit unitNumerator,
        EmissionUnit unitDenominator
        )
{
}
