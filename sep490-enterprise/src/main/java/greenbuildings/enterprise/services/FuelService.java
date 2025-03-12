package greenbuildings.enterprise.services;

import greenbuildings.enterprise.entities.FuelEntity;

import java.util.List;

public interface FuelService {
    List<FuelEntity> findAll();
}
