package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.EmissionFactorEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmissionFactorRepository extends JpaRepository<EmissionFactorEntity, UUID> {
    List<EmissionFactorEntity> findBySourceId(UUID sourceId);
    
    // With entity graph: nested association need to be loaded explicitly
    // ex: conversion.fuel
    // One query only: inner join with not-null fields and left join nullable fields
    @Override
    @EntityGraph(attributePaths = {"source", "energyConversion", "energyConversion.fuel"})
    List<EmissionFactorEntity> findAll();
    
}
