package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NewGroupItemDTO(
        UUID id,
        int version,
        @NotNull UUID buildingGroupId,
        String name,
        String description
) {
}
