package greenbuildings.enterprise.views.dashboard;

import java.math.BigDecimal;

/**
 * Pie Charts - Shows emissions from different sources
 */
public record DistributionEmissionSource(
        String sourceName,
        BigDecimal totalGhgEmission
) {
}
