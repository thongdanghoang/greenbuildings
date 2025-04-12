package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.dtos.EmissionFactorCriteriaDTO;
import greenbuildings.enterprise.entities.EmissionFactorEntity;
import greenbuildings.enterprise.repositories.EmissionFactorRepository;
import greenbuildings.enterprise.services.EmissionFactorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
@Slf4j
public class EmissionFactorServiceImpl implements EmissionFactorService {
    
    private final EmissionFactorRepository emissionFactorRepository;
    
    @Override
    @Cacheable(value = "emissionFactors")
    public Set<EmissionFactorEntity> findAllAvailable() {
        return new HashSet<>(emissionFactorRepository.findAllByActiveIsTrueAndEmissionUnitDenominatorNotNullAndEmissionUnitNumeratorNotNull());
    }
    
    @Override
    public List<EmissionFactorEntity> findBySource(UUID sourceId) {
        return emissionFactorRepository.findBySourceId(sourceId);
    }
    
    @Override
    public Optional<EmissionFactorEntity> findById(UUID id) {
        return emissionFactorRepository.findById(id);
    }
    
    @Override
    public Page<EmissionFactorEntity> search(SearchCriteriaDTO<EmissionFactorCriteriaDTO> searchCriteria, Pageable pageable) {
        var emissionFactorIDs = emissionFactorRepository.findByName(
                searchCriteria.criteria().criteria(),
                pageable);
        // Fetch each entity individually using findAllById(UUID id)
        var results = emissionFactorIDs.stream()
                                       .map(emissionFactorRepository::findAllById) // Fetch for each ID
                                       .flatMap(List::stream) // Flatten the list of lists
                                       .collect(Collectors.toMap(EmissionFactorEntity::getId, Function.identity()));
        return emissionFactorIDs.map(results::get);
    }
    
    @CacheEvict(value = "emissionFactors", allEntries = true)
    public void delete(UUID id) {
        var emissionFactor = emissionFactorRepository.findById(id).orElseThrow();
        if (emissionFactor.isActive()) {
            emissionFactor.setActive(false);
        } else {
            if (emissionFactor.getEmissionUnitNumerator() == null
                || emissionFactor.getEmissionUnitDenominator() == null
                || emissionFactor.getSource() == null) {
                throw new BusinessException("emissionFactor.disabled");
            }
            if (emissionFactor.isDirectEmission()
                && emissionFactor.getEnergyConversion() != null) {
                throw new BusinessException("emissionFactor.disabled");
            }
            emissionFactor.setActive(true);
        }
        emissionFactorRepository.save(emissionFactor);
    }
    
    @Override
    public void createOrUpdate(EmissionFactorEntity factor) {
        if (factor.getValidFrom().isAfter(factor.getValidTo())) {
            throw new BusinessException("emissionFactor.date");
        }
        emissionFactorRepository.save(factor);
    }
    
    
}
