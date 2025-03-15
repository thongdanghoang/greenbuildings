package greenbuildings.enterprise.services;

import greenbuildings.enterprise.entities.EmissionFactorEntity;

import java.util.List;
import java.util.UUID;

public interface EmissionFactorService {
    List<EmissionFactorEntity> findAll();

    List<EmissionFactorEntity> findBySource(UUID sourceId);
}
