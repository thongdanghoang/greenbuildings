package greenbuildings.enterprise.views.dashboard;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record DefaultChartView(
    BigDecimal totalEnterpriseEmissions,
    List<DistributionEmissionSource> distributionEmissionSources, // top 5 highest emission sources
    List<BuildingGhgEmission> buildingGhgEmissions // top 5 highest buildings emissions
) {
}
