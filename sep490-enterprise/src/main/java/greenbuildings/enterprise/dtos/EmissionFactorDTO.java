package greenbuildings.enterprise.dtos;


import greenbuildings.enterprise.enums.EmissionUnit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EmissionFactorDTO(
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
        EmissionSourceDTO emissionSourceDTO,
        String description,
        LocalDate validFrom,
        LocalDate validTo,
        boolean directEmission,
        EnergyConversionDTO energyConversionDTO,
        boolean active
) {}