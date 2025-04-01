package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
    public Set<EmissionFactorEntity> findAll() {
        return new HashSet<>(emissionFactorRepository.findAllByActiveIsTrue());
    }

    @Override
    public List<EmissionFactorEntity> findBySource(UUID sourceId) {
        return emissionFactorRepository.findBySourceId(sourceId);
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

}
