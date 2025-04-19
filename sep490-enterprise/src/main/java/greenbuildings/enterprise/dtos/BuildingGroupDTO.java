package greenbuildings.enterprise.dtos;

import greenbuildings.commons.api.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record BuildingGroupDTO(
        UUID id,
        int version,
        @NotBlank String name,
        String description,
        BuildingDTO building,
        TenantDTO tenant,
        Set<EmissionActivityDTO> emissionActivities
) implements BaseDTO {
} 