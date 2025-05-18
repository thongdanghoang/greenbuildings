package greenbuildings.enterprise.dtos;

import greenbuildings.enterprise.enums.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Map;

public record RevenueReportDTO(
        long numberOfPaidPayment,
        BigDecimal totalRevenue,
        long numberOfTransactions,
        Map<String, PaymentRevenueByQuarter> revenueByQuarter,
        Map<TransactionType, NumberOfTransactionByType> numberOfTransactionByType
) {
}
