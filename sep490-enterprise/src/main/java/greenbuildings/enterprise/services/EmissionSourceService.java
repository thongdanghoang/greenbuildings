package greenbuildings.enterprise.services;

import greenbuildings.enterprise.entities.EmissionSourceEntity;

import java.util.Set;

public interface EmissionSourceService {
    Set<EmissionSourceEntity> findAll();
}
