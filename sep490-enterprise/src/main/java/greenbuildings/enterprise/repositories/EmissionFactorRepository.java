package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.EmissionFactorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface EmissionFactorRepository extends JpaRepository<EmissionFactorEntity, UUID> {
    List<EmissionFactorEntity> findBySourceId(UUID sourceId);
    
    // With entity graph: nested association need to be loaded explicitly
    // ex: conversion.fuel
    // One query only: inner join with not-null fields and left join nullable fields
    @EntityGraph(attributePaths = {"source", "energyConversion", "energyConversion.fuel"})
    List<EmissionFactorEntity> findAllByActiveIsTrue();

    @Query("""
            SELECT e.id
            FROM EmissionFactorEntity e
            WHERE (LOWER(e.nameEN) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(e.nameVN) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(e.nameZH) LIKE LOWER(CONCAT('%', :name, '%')))
            """
    )
    Page<UUID> findByName(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"source", "energyConversion", "energyConversion.fuel"})
    List<EmissionFactorEntity> findAllById(UUID id);
    
}
