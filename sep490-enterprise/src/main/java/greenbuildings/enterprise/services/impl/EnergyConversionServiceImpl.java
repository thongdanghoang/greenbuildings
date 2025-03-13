package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.entities.EnergyConversionEntity;
import greenbuildings.enterprise.repositories.EnergyConversionRepository;
import greenbuildings.enterprise.services.EnergyConversionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional(rollbackOn = Throwable.class)
@RequiredArgsConstructor
public class EnergyConversionServiceImpl implements EnergyConversionService {
    
    private final EnergyConversionRepository energyConversionRepository;
    
    @Override
    public List<EnergyConversionEntity> findAll() {
        return energyConversionRepository.findAll();
    }
    
}
