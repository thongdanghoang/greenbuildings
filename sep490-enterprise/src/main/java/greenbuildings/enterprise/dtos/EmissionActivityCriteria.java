package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EmissionActivityCriteria(
        @NotNull UUID buildingId,
        UUID emissionSourceId,
        String name,
        String type,
        String category,
        String description,
        int quantity) {
}
