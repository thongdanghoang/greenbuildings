package greenbuildings.enterprise.services;

import greenbuildings.enterprise.entities.EnergyConversionEntity;

import java.util.List;

public interface EnergyConversionService {
    List<EnergyConversionEntity> findAll();
}
