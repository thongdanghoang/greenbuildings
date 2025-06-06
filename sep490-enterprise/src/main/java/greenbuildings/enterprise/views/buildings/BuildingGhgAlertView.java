package greenbuildings.enterprise.views.buildings;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record BuildingGhgAlertView(
        UUID id,
        BigDecimal ghgEmission,
        BigDecimal limit,
        BigDecimal percentage
) {
}
