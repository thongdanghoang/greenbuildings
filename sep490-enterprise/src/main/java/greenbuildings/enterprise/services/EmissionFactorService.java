package greenbuildings.enterprise.services;

import greenbuildings.enterprise.entities.EmissionFactorEntity;

import java.util.List;

public interface EmissionFactorService {
    List<EmissionFactorEntity> findAll();
}
