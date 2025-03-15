package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.entities.FuelEntity;
import greenbuildings.enterprise.repositories.FuelRepository;
import greenbuildings.enterprise.services.FuelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional(rollbackOn = Throwable.class)
@Slf4j
@RequiredArgsConstructor
public class FuelServiceImpl implements FuelService {
    
    private final FuelRepository fuelRepository;
    
    @Override
    @Cacheable("fuels")
    public Set<FuelEntity> findAll() {
        return new HashSet<>(fuelRepository.findAll());
    }
    
    
}
