package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record BuildingGroupCriteria(
        @NotNull UUID buildingId,
        UUID tenantId,
        String name
) {
}
