package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.entities.ChemicalDensityEntity;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.entities.EmissionFactorEntity;
import greenbuildings.enterprise.entities.EnergyConversionEntity;
import greenbuildings.enterprise.enums.EmissionUnit;
import greenbuildings.enterprise.enums.UnitCategory;
import greenbuildings.enterprise.repositories.ChemicalDensityRepository;
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
    private final ChemicalDensityRepository chemicalDensityRepo;
    
    @Override
    public List<EmissionActivityRecordEntity> calculate(UUID activityId, List<EmissionActivityRecordEntity> content) {
        EmissionActivityEntity activity = activityRepo.findById(activityId).orElseThrow();
        EmissionFactorEntity factor = activity.getEmissionFactorEntity();
        
        if (factor == null || !factor.isActive() || content.isEmpty()) {
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
        
        if (outputFactor.getCategory().equals(UnitCategory.MASS)) {
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
        } else if (outputFactor.getCategory().equals(UnitCategory.VOLUME)) {
            List<ChemicalDensityEntity> densities = chemicalDensityRepo.findAll();
            // input unit -> volume -> volume cubic -> kg -> utils -> final
            ChemicalDensityEntity co2 = densities.stream().filter(x -> x.getChemicalFormula().equals("CO2")).findFirst().orElseThrow();
            ChemicalDensityEntity ch4 = densities.stream().filter(x -> x.getChemicalFormula().equals("CH4")).findFirst().orElseThrow();
            ChemicalDensityEntity n2o = densities.stream().filter(x -> x.getChemicalFormula().equals("N2O")).findFirst().orElseThrow();
            
            Pair<EmissionUnit, BigDecimal>  co2Volume = Pair.of(outputFactor, factorReadyValue.getRight().multiply(factor.getCo2()));
            Pair<EmissionUnit, BigDecimal>  ch4Volume = Pair.of(outputFactor, factorReadyValue.getRight().multiply(factor.getCh4()));
            Pair<EmissionUnit, BigDecimal>  n2oVolume = Pair.of(outputFactor, factorReadyValue.getRight().multiply(factor.getN2o()));
            
            co2Volume = Pair.of(co2.getUnitDenominator(), CalculationUtils.convertUnit(co2Volume.getLeft(), co2.getUnitDenominator(), co2Volume.getRight()));
            ch4Volume = Pair.of(ch4.getUnitDenominator(), CalculationUtils.convertUnit(ch4Volume.getLeft(), ch4.getUnitDenominator(), ch4Volume.getRight()));
            n2oVolume = Pair.of(n2o.getUnitDenominator(), CalculationUtils.convertUnit(n2oVolume.getLeft(), n2o.getUnitDenominator(), n2oVolume.getRight()));
            
            Pair<EmissionUnit, BigDecimal> co2Mass = Pair.of(co2.getUnitNumerator(), co2Volume.getRight().multiply(BigDecimal.valueOf(co2.getValue())));
            Pair<EmissionUnit, BigDecimal> n2oMass = Pair.of(n2o.getUnitNumerator(), n2oVolume.getRight().multiply(BigDecimal.valueOf(n2o.getValue())));
            Pair<EmissionUnit, BigDecimal> ch4Mass = Pair.of(ch4.getUnitNumerator(), ch4Volume.getRight().multiply(BigDecimal.valueOf(ch4.getValue())));
            
            co2Mass = Pair.of(EmissionUnit.KILOGRAM, CalculationUtils.convertUnit(co2Mass.getLeft(), EmissionUnit.KILOGRAM, co2Mass.getRight()));
            n2oMass = Pair.of(EmissionUnit.KILOGRAM, CalculationUtils.convertUnit(n2oMass.getLeft(), EmissionUnit.KILOGRAM, n2oMass.getRight()));
            ch4Mass = Pair.of(EmissionUnit.KILOGRAM, CalculationUtils.convertUnit(ch4Mass.getLeft(), EmissionUnit.KILOGRAM, ch4Mass.getRight()));
            
            Pair<EmissionUnit, BigDecimal> finalResult = Pair.of(EmissionUnit.KILOGRAM, CalculationUtils.calculateCO2e(co2Mass.getRight(),ch4Mass.getRight(), n2oMass.getRight()));
            return finalResult.getRight();
        }
        log.error("Mismatch unit of factor unit");
        return BigDecimal.ZERO;
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
