package greenbuildings.enterprise.services.impl;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionSourceCriteriaDTO;

import greenbuildings.enterprise.entities.EmissionSourceEntity;
import greenbuildings.enterprise.repositories.EmissionSourceRepository;
import greenbuildings.enterprise.services.EmissionSourceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public Page<EmissionSourceEntity> search(SearchCriteriaDTO<EmissionSourceCriteriaDTO> searchCriteria, Pageable pageable) {
        var emissionSourceIDs = emissionSourceRepository.findByName(
                searchCriteria.criteria().criteria(),
               pageable);
        var results = emissionSourceRepository.findAllById(emissionSourceIDs.toSet())
                .stream()
                .collect(Collectors.toMap(EmissionSourceEntity::getId, Function.identity()));
        return emissionSourceIDs.map(results::get);
    }

    @Override
    public void createOrUpdate(EmissionSourceEntity emissionSourceEntity) {
        emissionSourceRepository.save(emissionSourceEntity);
    }

    @Override
    public Optional<EmissionSourceEntity> findById(UUID id) {
        return emissionSourceRepository.findById(id);
    }
}
