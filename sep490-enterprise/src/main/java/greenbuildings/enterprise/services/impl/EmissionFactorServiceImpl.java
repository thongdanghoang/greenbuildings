package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.entities.EmissionFactorEntity;
import greenbuildings.enterprise.repositories.EmissionFactorRepository;
import greenbuildings.enterprise.services.EmissionFactorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
@Slf4j
public class EmissionFactorServiceImpl implements EmissionFactorService {
    
    private final EmissionFactorRepository emissionFactorRepository;
    
    @Override
    @Cacheable("emissionFactors")
    public Set<EmissionFactorEntity> findAll() {
        return new HashSet<>(emissionFactorRepository.findAllByActiveIsTrue());
    }

    @Override
    public List<EmissionFactorEntity> findBySource(UUID sourceId) {
        return emissionFactorRepository.findBySourceId(sourceId);
    }

}
