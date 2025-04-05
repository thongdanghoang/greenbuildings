package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.dtos.EmissionFactorCriteriaDTO;
import greenbuildings.enterprise.entities.EmissionFactorEntity;
import greenbuildings.enterprise.repositories.EmissionFactorRepository;
import greenbuildings.enterprise.services.EmissionFactorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @Cacheable("emissionFactors")
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

    public void delete(UUID id) {
        var emissionFactor = emissionFactorRepository.findById(id).orElse(null);
        if(!Objects.requireNonNull(emissionFactor).isActive()) {
            if(Objects.requireNonNull(emissionFactor).getEmissionUnitNumerator() == null || Objects.requireNonNull(emissionFactor).getEmissionUnitDenominator() == null || Objects.requireNonNull(emissionFactor).getSource() == null) {
                throw new BusinessException("emissionFactor","emissionFactor.disabled");
            }
            if(!Objects.requireNonNull(emissionFactor).isDirectEmission() || Objects.requireNonNull(emissionFactor).getEnergyConversion().getFuel() == null){
                throw new BusinessException("emissionFactor","emissionFactor.disabled");
            }
        }
        Objects.requireNonNull(emissionFactor).setActive(!emissionFactor.isActive());
        emissionFactorRepository.save(emissionFactor);
    }

    @Override
    public void createOrUpdate(EmissionFactorEntity factor) {
        if ( factor.getValidFrom().isAfter(factor.getValidTo())) {
            throw new BusinessException("emissionFactor","emissionFactor.date");
        }
        emissionFactorRepository.save(factor);
    }


}
