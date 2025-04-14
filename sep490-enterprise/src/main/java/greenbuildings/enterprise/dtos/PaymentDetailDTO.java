package greenbuildings.enterprise.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import greenbuildings.commons.api.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentDetailDTO(
        UUID id,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDateTime createdDate,
        PaymentStatus status,
        long amount,
        Integer numberOfCredits,
        String bin,
        String accountNumber,
        String accountName,
        String description,
        Long orderCode,
        String currency,
        String paymentLinkId,
        String payOSStatus,
        Long expiredAt,
        String checkoutUrl,
        String qrCode,
        CreditPackageVersionDTO creditPackageVersionDTO,
        EnterpriseDTO enterpriseDTO
) {
}
