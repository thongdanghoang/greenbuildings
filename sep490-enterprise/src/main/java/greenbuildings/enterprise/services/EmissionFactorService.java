package greenbuildings.enterprise.services;

import greenbuildings.enterprise.entities.EmissionFactorEntity;

import java.util.Set;

public interface EmissionFactorService {
    Set<EmissionFactorEntity> findAll();
}
