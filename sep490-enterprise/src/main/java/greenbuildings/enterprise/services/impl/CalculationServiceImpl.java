package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.entities.EmissionFactorEntity;
import greenbuildings.enterprise.entities.EnergyConversionEntity;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.services.CalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
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
    
    
    }
    
    private void calculateIndirectly(EmissionFactorEntity factor, List<EmissionActivityRecordEntity> content) {
        EnergyConversionEntity energyConversion = Optional.ofNullable(factor.getEnergyConversion()).orElseThrow();
        
        
    }
    
    
    
}
