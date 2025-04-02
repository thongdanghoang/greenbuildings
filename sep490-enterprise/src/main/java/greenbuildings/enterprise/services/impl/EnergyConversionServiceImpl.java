package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.FuelCriteriaDTO;
import greenbuildings.enterprise.entities.EmissionSourceEntity;
import greenbuildings.enterprise.entities.EnergyConversionEntity;
import greenbuildings.enterprise.entities.FuelEntity;
import greenbuildings.enterprise.repositories.EnergyConversionRepository;
import greenbuildings.enterprise.repositories.FuelRepository;
import greenbuildings.enterprise.services.EnergyConversionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class EnergyConversionServiceImpl implements EnergyConversionService {
    
    private final EnergyConversionRepository energyConversionRepository;
    private final FuelRepository fuelRepository;

    @Override
    public List<EnergyConversionEntity> findAll() {
        return energyConversionRepository.findAll();
    }

    @Override
    public Page<EnergyConversionEntity> search(SearchCriteriaDTO<FuelCriteriaDTO> searchCriteria, Pageable pageable) {
        var fuelIDs = energyConversionRepository.findByName(
                searchCriteria.criteria().criteria(),
                pageable);
        var results = energyConversionRepository.findAllById(fuelIDs.toSet())
                .stream()
                .collect(Collectors.toMap(EnergyConversionEntity::getId, Function.identity()));
        return fuelIDs.map(results::get);
    }
    @Override
    public void createOrUpdate(EnergyConversionEntity energyConversionEntity) {
        if (energyConversionEntity.getId() == null) {
            FuelEntity fuelEntity = new FuelEntity();
            fuelEntity.setNameEN(energyConversionEntity.getFuel().getNameEN());
            fuelEntity.setNameZH(energyConversionEntity.getFuel().getNameZH());
            fuelEntity.setNameVN(energyConversionEntity.getFuel().getNameVN());
            FuelEntity fuelEntity1 = fuelRepository.save(fuelEntity);
            energyConversionEntity.setFuel(fuelEntity1);
        }
        else{
            FuelEntity fuelOld = fuelRepository.findById(energyConversionEntity.getFuel().getId()).get();
            fuelOld.setNameEN(energyConversionEntity.getFuel().getNameEN());
            fuelOld.setNameZH(energyConversionEntity.getFuel().getNameZH());
            fuelOld.setNameVN(energyConversionEntity.getFuel().getNameVN());
            FuelEntity fuelEntity = fuelRepository.save(fuelOld);
            energyConversionEntity.setFuel(fuelEntity);
        }
        energyConversionRepository.save(energyConversionEntity);
    }
    @Override
    public Optional<EnergyConversionEntity> findById(UUID id) {
        return energyConversionRepository.findById(id);
    }
}
