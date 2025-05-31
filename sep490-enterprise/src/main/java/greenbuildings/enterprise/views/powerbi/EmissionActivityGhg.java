package greenbuildings.enterprise.views.powerbi;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record EmissionActivityGhg(
        String name,
        String category,
        String typeName,
        String buildingName,
        String buildingGroupName,
        String tenantName,
        String factorNameVN,
        String factorNameEN,
        String factorNameZH,
        LocalDateTime factorValidFrom,
        LocalDateTime factorValidTo,
        String sourceNameVN,
        String sourceNameEN,
        String sourceNameZH,
        BigDecimal ghg
) {
}
