package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record CreateEmissionActivityDTO(
        UUID id,
        int version,
        @NotNull UUID buildingId,
        UUID buildingGroupID,
        Set<EmissionActivityRecordDTO> records,
        @NotNull UUID emissionFactorID,
        @NotEmpty String name,
        String type,
        String category,
        String description
) {
}
