package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record EmissionActivityDTO (
        UUID id,
        int version,
        @NotNull UUID buildingID,
        Set<EmissionActivityRecordDTO> records,
        @NotNull UUID emissionFactorID,
        @NotEmpty String name,
        String type,
        String category,
        String description,
        @Min(0) int quantity
) {
}
