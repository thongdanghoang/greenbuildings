package greenbuildings.enterprise.dtos;

import greenbuildings.enterprise.enums.EmissionUnit;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record NewEmissionActivityRecordDTO(
        UUID id,
        int version,
        UUID activityId,
        BigDecimal value,
        EmissionUnit unit,
        LocalDateTime startDate,
        LocalDateTime endDate,
        @Min(0) int quantity
) {
}
