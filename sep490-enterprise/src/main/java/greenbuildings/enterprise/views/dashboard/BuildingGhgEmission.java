package greenbuildings.enterprise.views.dashboard;

import java.math.BigDecimal;

/**
 * Bar Charts - Display the emissions of each building belonging to that business
 */
public record BuildingGhgEmission(
        String buildingName,
        BigDecimal totalGhgEmission
) {
}
