package greenbuildings.enterprise.dtos;

import greenbuildings.enterprise.enums.EmissionUnit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EmissionActivityRecordDTO(
        UUID id,
        int version,
        BigDecimal value,
        EmissionUnit unit,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
