package greenbuildings.enterprise.services;

import greenbuildings.enterprise.entities.EmissionSourceEntity;

import java.util.List;

public interface EmissionSourceService {
    List<EmissionSourceEntity> findAll();
}
