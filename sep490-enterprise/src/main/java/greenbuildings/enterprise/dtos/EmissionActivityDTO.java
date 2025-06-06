package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record EmissionActivityDTO (
        UUID id,
        int version,
        @NotNull UUID buildingGroupID,
        Set<EmissionActivityRecordDTO> records,
        @NotNull UUID emissionFactorID,
        @NotEmpty String name,
        ActivityTypeDTO type,
        @NotBlank String category,
        String description
) {
}
