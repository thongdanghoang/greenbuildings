package greenbuildings.enterprise.dtos;

import greenbuildings.commons.api.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

import java.util.Set;
import java.util.UUID;

@Builder
public record BuildingGroupDTO(
        UUID id,
        int version,
        @NotBlank String name,
        @NotNull UUID buildingId,
        @With UUID tenantId,
        Set<EmissionActivityDTO> emissionActivities
) implements BaseDTO {
} 