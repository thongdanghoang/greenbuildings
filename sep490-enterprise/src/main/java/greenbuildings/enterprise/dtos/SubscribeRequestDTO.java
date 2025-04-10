package greenbuildings.enterprise.dtos;

import greenbuildings.enterprise.enums.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record SubscribeRequestDTO(
        UUID buildingId,
        @Min(0) int months,
        @Min(0) @Positive int numberOfDevices,
        double monthRatio,
        double deviceRatio,
        TransactionType type) {
}
