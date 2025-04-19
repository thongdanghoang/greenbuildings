package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EmissionActivityCriteria(
        @NotNull UUID buildingGroupId,
        UUID emissionSourceId,
        String name) {
}
