package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.entities.EmissionSourceEntity;
import greenbuildings.enterprise.repositories.EmissionSourceRepository;
import greenbuildings.enterprise.services.EmissionSourceService;
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
public class EmissionSourceServiceImpl implements EmissionSourceService {
    
    private final EmissionSourceRepository emissionSourceRepository;
    
    @Override
    @Cacheable("emissionSources")
    public Set<EmissionSourceEntity> findAll() {
        return new HashSet<>(emissionSourceRepository.findAll());
    }
    
    
}
