package greenbuildings.enterprise.dtos;

import greenbuildings.enterprise.enums.EmissionUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ChemicalDensityDTO(
        UUID id,
        int version,
        @NotBlank String chemicalFormula,
        @Positive double value,
        @NotNull EmissionUnit unitNumerator,
        @NotNull EmissionUnit unitDenominator
) {
}
