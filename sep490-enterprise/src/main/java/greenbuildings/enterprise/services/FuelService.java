package greenbuildings.enterprise.services;

import greenbuildings.enterprise.entities.FuelEntity;

import java.util.Set;

public interface FuelService {
    Set<FuelEntity> findAll();
}
