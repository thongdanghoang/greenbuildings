package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.dtos.ChemicalDensityCriteria;
import greenbuildings.enterprise.entities.ActivityTypeEntity;
import greenbuildings.enterprise.entities.ChemicalDensityEntity;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.repositories.ChemicalDensityRepository;
import greenbuildings.enterprise.services.ChemicalDensityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Throwable.class)
public class ChemicalDensityServiceImpl implements ChemicalDensityService {
    private final ChemicalDensityRepository chemicalDensityRepo;

    @Override
    public Page<ChemicalDensityEntity> search(SearchCriteriaDTO<ChemicalDensityCriteria> searchCriteria, Pageable pageable) {
        var chemicalDensityIDs = chemicalDensityRepo.findByName(
                searchCriteria.criteria().criteria(),
                pageable);
        var results = chemicalDensityRepo.findAllById(chemicalDensityIDs.toSet())
                .stream()
                .collect(Collectors.toMap(ChemicalDensityEntity::getId, Function.identity()));
        return chemicalDensityIDs.map(results::get);
    }

    @Override
    public void createOrUpdate(ChemicalDensityEntity chemicalDensityEntity) {
        chemicalDensityRepo.save(chemicalDensityEntity);
    }

    @Override
    public Optional<ChemicalDensityEntity> findById(UUID id) {
        return chemicalDensityRepo.findById(id);
    }

    @Override
    public void delete(Set<UUID> typeIds) {
        // Validate input
        if (CollectionUtils.isEmpty(typeIds)) {
            throw new BusinessException("activityType", "activityType.delete.no.ids", Collections.emptyList());
        }

        // Fetch all types in one query
        List<ChemicalDensityEntity> chemicalDensityEntities = chemicalDensityRepo.findAllById(typeIds);
        if (chemicalDensityEntities.isEmpty()) {
            return; // Nothing to delete
        }

        // Delete all types in one operation
        chemicalDensityRepo.deleteAll(chemicalDensityEntities);
    }
}
