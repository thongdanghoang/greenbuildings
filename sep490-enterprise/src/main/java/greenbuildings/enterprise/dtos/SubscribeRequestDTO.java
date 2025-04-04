package greenbuildings.enterprise.dtos;

import green_buildings.commons.api.validators.MultipleOf;
import greenbuildings.enterprise.enums.TransactionType;
import jakarta.validation.constraints.Min;

import java.util.UUID;

public record SubscribeRequestDTO(
        UUID buildingId,
        @Min(0) int months,
        @Min(0) @MultipleOf(50) int numberOfDevices,
        double monthRatio,
        double deviceRatio,
        TransactionType type) {
}
