package greenbuildings.enterprise.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import greenbuildings.enterprise.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDTO(
        UUID id,
        int version,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDateTime createdDate,
        double amount,
        int months,
        int numberOfDevices,
        TransactionType transactionType
) {
}
