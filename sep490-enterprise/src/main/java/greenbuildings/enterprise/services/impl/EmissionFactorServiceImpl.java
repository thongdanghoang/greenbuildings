package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.entities.EmissionFactorEntity;
import greenbuildings.enterprise.repositories.EmissionFactorRepository;
import greenbuildings.enterprise.services.EmissionFactorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Throwable.class)
@Slf4j
public class EmissionFactorServiceImpl implements EmissionFactorService {
    
    private final EmissionFactorRepository emissionFactorRepository;
    
    @Override
    @Cacheable("emissionFactors")
    public Set<EmissionFactorEntity> findAll() {
        return new HashSet<>(emissionFactorRepository.findAll());
    }
    
}
