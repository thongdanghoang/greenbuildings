package greenbuildings.enterprise.dtos;

import greenbuildings.enterprise.enums.EmissionUnit;

import java.math.BigDecimal;
import java.util.UUID;

public record EnergyConversionDTO(
        UUID id,
        FuelDTO fuel,
        BigDecimal conversionValue,
        EmissionUnit conversionUnitNumerator,
        EmissionUnit conversionUnitDenominator,
        int version
) {}