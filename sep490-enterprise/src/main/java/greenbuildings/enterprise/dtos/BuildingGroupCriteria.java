package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BuildingGroupCriteria(
        @NotNull UUID buildingId,
        UUID tenantId,
        String name
) {
}
