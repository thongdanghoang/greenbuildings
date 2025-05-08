package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;
import java.util.UUID;

public record EmissionActivityTableView(
        UUID id,
        int version,
        Set<EmissionActivityRecordDTO> records,
        EmissionFactorDTO emissionFactor,
        @NotEmpty String name,
        ActivityTypeDTO type,
        @NotBlank String category,
        String description
) {
}
