package greenbuildings.enterprise.repositories;

import greenbuildings.enterprise.entities.EmissionFactorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface EmissionFactorRepository extends JpaRepository<EmissionFactorEntity, UUID> {
    List<EmissionFactorEntity> findBySourceId(UUID sourceId);
    
    // With entity graph: nested association need to be loaded explicitly
    // ex: conversion.fuel
    // One query only: inner join with not-null fields and left join nullable fields
    @EntityGraph(attributePaths = {"source", "energyConversion", "energyConversion.fuel"})
    List<EmissionFactorEntity> findAllByActiveIsTrueAndEmissionUnitDenominatorNotNullAndEmissionUnitNumeratorNotNull();

    @Query("""
        SELECT e.id
        FROM EmissionFactorEntity e
        WHERE (LOWER(e.nameEN) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(e.nameVN) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(e.nameZH) LIKE LOWER(CONCAT('%', :name, '%')))
        ORDER BY e.active desc\s
       \s"""
    )
    Page<UUID> findByName(String name, Pageable pageable);

    List<EmissionFactorEntity> findAllById(UUID id);

    @Query("""
        SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END
        FROM EmissionFactorEntity e
        WHERE LOWER(e.nameVN) = LOWER(:nameVN)
           AND LOWER(e.nameEN) = LOWER(:nameEN)
           AND LOWER(e.nameZH) = LOWER(:nameZH)
           AND e.co2 = :co2
           AND e.ch4 = :ch4
           AND e.n2o = :n2o
           AND LOWER(e.emissionUnitNumerator) = LOWER(:emissionUnitNumerator)
           AND LOWER(e.emissionUnitDenominator) = LOWER(:emissionUnitDenominator)
           AND LOWER(e.source.nameEN) = LOWER(:sourceEN)
           AND LOWER(e.source.nameVN) = LOWER(:sourceVN)
           AND LOWER(e.source.nameZH) = LOWER(:sourceZH)
           AND LOWER(e.energyConversion.fuel.nameEN) = LOWER(:fuelEN)
           AND LOWER(e.energyConversion.fuel.nameVN) = LOWER(:fuelVN)
           AND LOWER(e.energyConversion.fuel.nameZH) = LOWER(:fuelZH)
           AND e.energyConversion.conversionValue = :conversionValue
           AND LOWER(e.energyConversion.conversionUnitNumerator) = LOWER(:unitNumeratorConversion)
           AND LOWER(e.energyConversion.conversionUnitDenominator) = LOWER(:unitDenominatorConversion)
        """
    )
    boolean existsByEmissionFactorFull(String nameVN, String nameEN, String nameZH, BigDecimal co2, BigDecimal ch4, BigDecimal n2o, String emissionUnitNumerator, String emissionUnitDenominator, String sourceVN, String sourceEN, String sourceZH, String fuelVN, String fuelEN, String fuelZH, BigDecimal conversionValue, String unitNumeratorConversion, String unitDenominatorConversion);


    @Query("""
        SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END
        FROM EmissionFactorEntity e
        WHERE LOWER(e.nameVN) = LOWER(:nameVN)
           AND LOWER(e.nameEN) = LOWER(:nameEN)
           AND LOWER(e.nameZH) = LOWER(:nameZH)
           AND CAST(e.co2 AS double) = CAST(:co2 AS double)
           AND CAST(e.ch4 AS double) = CAST(:ch4 AS double)
           AND CAST(e.n2o AS double) = CAST(:n2o AS double)
           AND LOWER(e.emissionUnitNumerator) = LOWER(:emissionUnitNumerator)
           AND LOWER(e.emissionUnitDenominator) = LOWER(:emissionUnitDenominator)
        """
    )
    boolean existsByEmissionFactor(String nameVN, String nameEN, String nameZH, BigDecimal co2, BigDecimal ch4, BigDecimal n2o, String emissionUnitNumerator, String emissionUnitDenominator);
    
    @Query("""
            SELECT activity.emissionFactorEntity
            FROM EmissionActivityEntity activity
            WHERE activity.building.enterprise.id = :enterpriseId
            AND activity.emissionFactorEntity.active = true
            """)
    List<EmissionFactorEntity> getEmissionFactorsByEnterpriseId(UUID enterpriseId);
}
