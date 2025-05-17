package greenbuildings.enterprise.views.emission_activities;

import greenbuildings.enterprise.enums.EmissionUnit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record EmissionActivityView(
        UUID id,
        int version,
        EmissionActivityBuildingView building,
        EmissionActivityBuildingGroupView buildingGroup,
        EmissionActivityFactorView emissionFactor,
        String type,
        String name,
        String category,
        String description,
        List<EmissionActivityRecordView> records
) {
    public record EmissionActivityBuildingView(
            UUID id,
            int version,
            String name
    ) {
    }
    
    public record EmissionActivityBuildingGroupView(
            UUID id,
            int version,
            String name
    ) {
    }
    
    public record EmissionActivityFactorView(
            UUID id,
            int version,
            BigDecimal co2,
            BigDecimal ch4,
            BigDecimal n2o,
            String nameVN,
            String nameEN,
            String nameZH,
            EmissionUnit emissionUnitNumerator,
            EmissionUnit emissionUnitDenominator,
            String description,
            LocalDate validFrom,
            LocalDate validTo,
            boolean directEmission,
            boolean active,
            EmissionSourceView emissionSourceDTO
    ) {
        public record EmissionSourceView(
                UUID id,
                int version,
                String nameVN,
                String nameEN,
                String nameZH
        ) {
        }
    }
    
    public record EmissionActivityRecordView(
            UUID id,
            int version,
            BigDecimal ghg,
            BigDecimal value,
            EmissionUnit unit,
            LocalDate startDate,
            LocalDate endDate,
            RecordFileView file,
            int quantity,
            UUID assetId
    ) {
        public record RecordFileView(
                UUID id,
                String fileName,
                String contentType
        ) {
        }
    }
}
