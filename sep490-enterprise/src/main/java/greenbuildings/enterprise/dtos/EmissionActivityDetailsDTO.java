package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public record EmissionActivityDetailsDTO(
        UUID id,
        int version,
        @NotNull UUID buildingId,
        BuildingGroupDTO buildingGroup,
        @NotNull EmissionFactorDTO emissionFactor,
        Set<EmissionActivityRecordDTO> records,
        @NotBlank @Size(max = 255) String name,
        @Size(max = 255) ActivityTypeDTO type,
        @Size(max = 255) String category,
        @Min(0) int quantity,
        @Size(max = 1000) String description
) {
} 