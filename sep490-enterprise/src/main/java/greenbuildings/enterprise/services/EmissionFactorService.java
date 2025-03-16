package greenbuildings.enterprise.services;

import greenbuildings.enterprise.entities.EmissionFactorEntity;

import java.util.List;
import java.util.UUID;
import java.util.Set;

public interface EmissionFactorService {
    Set<EmissionFactorEntity> findAll();

    List<EmissionFactorEntity> findBySource(UUID sourceId);
}
