package greenbuildings.enterprise.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import greenbuildings.commons.api.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentEnterpriseAdminDTO(
        UUID id,
        int version,
        String enterpriseName,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDateTime createdDate,
        Integer numberOfCredits,
        PaymentStatus status
) {
}
