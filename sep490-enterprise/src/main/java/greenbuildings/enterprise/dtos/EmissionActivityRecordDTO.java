package greenbuildings.enterprise.dtos;

import greenbuildings.enterprise.enums.EmissionUnit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EmissionActivityRecordDTO(
        UUID id,
        int version,
        BigDecimal value,
        EmissionUnit unit,
        LocalDate startDate,
        LocalDate endDate,
        RecordFileDTO file,
        int quantity,
        BigDecimal ghg
) {
}
