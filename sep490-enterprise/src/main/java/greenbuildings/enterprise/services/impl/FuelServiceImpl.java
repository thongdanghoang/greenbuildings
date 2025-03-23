package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.FuelCriteriaDTO;
import greenbuildings.enterprise.dtos.FuelDTO;
import greenbuildings.enterprise.entities.FuelEntity;
import greenbuildings.enterprise.repositories.FuelRepository;
import greenbuildings.enterprise.services.FuelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public Page<FuelEntity> search(SearchCriteriaDTO<FuelCriteriaDTO> searchCriteria, Pageable pageable) {
        var fuelIDs = fuelRepository.findByName(
                searchCriteria.criteria().criteria(),
                pageable);
        var results = fuelRepository.findAllById(fuelIDs.toSet())
                .stream()
                .collect(Collectors.toMap(FuelEntity::getId, Function.identity()));
        return fuelIDs.map(results::get);
    }
}
