package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.entities.EmissionFactorEntity;
import greenbuildings.enterprise.entities.EnergyConversionEntity;
import greenbuildings.enterprise.enums.EmissionUnit;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.services.CalculationService;
import greenbuildings.enterprise.utils.CalculationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalculationServiceImpl implements CalculationService {
    
    private final EmissionActivityRepository activityRepo;
    
    @Override
    public List<EmissionActivityRecordEntity> calculate(UUID activityId, List<EmissionActivityRecordEntity> content) {
        EmissionActivityEntity activity = activityRepo.findById(activityId).orElseThrow();
        EmissionFactorEntity factor = activity.getEmissionFactorEntity();
        
        if (factor == null || !factor.isActive() || activity.getRecords().isEmpty()) {
            return content;
        }
        if (factor.isDirectEmission()) {
            calculateDirectly(factor, content);
        } else {
            calculateIndirectly(factor, content);
        }
        return content;
    }
    
    private void calculateDirectly(EmissionFactorEntity factor, List<EmissionActivityRecordEntity> content) {
        for (EmissionActivityRecordEntity entity : content) {
            entity.setGhg(calculateDirectly(factor, entity));
        }
    }
    
    private BigDecimal calculateDirectly(EmissionFactorEntity factor, EmissionActivityRecordEntity recordEntity) {
        EmissionUnit inputFactor = factor.getEmissionUnitDenominator();
        EmissionUnit outputFactor = factor.getEmissionUnitNumerator();
        
        if (inputFactor == null || outputFactor == null) {
            return BigDecimal.ZERO;
        }
        if (!recordEntity.getUnit().isSameCategory(inputFactor)) {
            log.error("Mismatch unit of conversion and factor");
            return BigDecimal.ZERO;
        }
        
        Pair<EmissionUnit, BigDecimal> rawMeasurement = Pair.of(recordEntity.getUnit(), recordEntity.getValue());
        Pair<EmissionUnit, BigDecimal> factorReadyValue = Pair.of(inputFactor, CalculationUtils.convertUnit(
                rawMeasurement.getLeft(), inputFactor, rawMeasurement.getRight()));
        
        Pair<EmissionUnit, BigDecimal> emissionResult = Pair.of(
                outputFactor,  // typically KILOGRAM for CO2e
                CalculationUtils.calculateCO2e(
                        factor.getCo2().multiply(factorReadyValue.getRight()),   // CO2 contribution
                        factor.getCh4().multiply(factorReadyValue.getRight()),   // CH4 contribution
                        factor.getN2o().multiply(factorReadyValue.getRight())));   // N2O contribution
        
        Pair<EmissionUnit, BigDecimal> kilogramResult = Pair.of(
                EmissionUnit.KILOGRAM,
                CalculationUtils.convertUnit(emissionResult.getLeft(), EmissionUnit.KILOGRAM, emissionResult.getRight()));
        
        return kilogramResult.getRight();
    }
    
    private void calculateIndirectly(EmissionFactorEntity factor, List<EmissionActivityRecordEntity> content) {
        for (EmissionActivityRecordEntity entity : content) {
            entity.setGhg(calculateIndirectly(factor, entity));
        }
    }
    
    private BigDecimal calculateIndirectly(EmissionFactorEntity factor, EmissionActivityRecordEntity recordEntity) {
        EmissionUnit inputFactor = factor.getEmissionUnitDenominator(); // TERAJOULE
        EmissionUnit outputFactor = factor.getEmissionUnitNumerator(); // KILOGRAM
        
        EnergyConversionEntity energyConversion = Optional.ofNullable(factor.getEnergyConversion()).orElseThrow();
        EmissionUnit inputConversion = energyConversion.getConversionUnitDenominator(); // GIGAGRAM-MEGAGRAM - THOUSAND_CUBIC_METER
        EmissionUnit outputConversion = energyConversion.getConversionUnitNumerator(); // TERAJOULE - null
        
        if (!inputFactor.isSameCategory(outputConversion)) {
            log.error("Mismatch unit of conversion and factor");
            return BigDecimal.ZERO;
        }
        
        // Step 1: Extract raw measurement data
        Pair<EmissionUnit, BigDecimal> rawMeasurement = Pair.of(recordEntity.getUnit(), recordEntity.getValue());
        
        // Step 2: Standardize units for calculation
        Pair<EmissionUnit, BigDecimal> standardizedMeasurement = Pair.of(
                inputConversion,
                CalculationUtils.convertUnit(
                        rawMeasurement.getLeft(),  // original unit
                        inputConversion,           // target unit
                        rawMeasurement.getRight())); // value to convert
        
        
        // Step 3: Apply energy conversion factor
        Pair<EmissionUnit, BigDecimal> energyValue = Pair.of(
                outputConversion,
                energyConversion.getConversionValue().multiply(standardizedMeasurement.getRight()));
        
        
        // Step 4: Convert from energy conversion output unit to emission factor input unit
        Pair<EmissionUnit, BigDecimal> factorReadyValue = Pair.of(
                inputFactor,
                CalculationUtils.convertUnit(
                        energyValue.getLeft(),    // from outputConversion unit
                        inputFactor,              // to inputFactor unit
                        energyValue.getRight())); // value to convert
        
        
        // Step 5: Calculate final CO2 equivalent emissions
        Pair<EmissionUnit, BigDecimal> emissionResult = Pair.of(
                outputFactor,  // typically KILOGRAM for CO2e
                CalculationUtils.calculateCO2e(
                        factor.getCo2().multiply(factorReadyValue.getRight()),   // CO2 contribution
                        factor.getCh4().multiply(factorReadyValue.getRight()),   // CH4 contribution
                        factor.getN2o().multiply(factorReadyValue.getRight())));   // N2O contribution
        
        Pair<EmissionUnit, BigDecimal> kilogramResult = Pair.of(
                EmissionUnit.KILOGRAM,
                CalculationUtils.convertUnit(emissionResult.getLeft(), EmissionUnit.KILOGRAM, emissionResult.getRight()));
        
        // Update the recordEntity with calculated GHG value
        return kilogramResult.getRight();
    }
    
}
