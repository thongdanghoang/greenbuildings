package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.views.powerbi.EmissionActivityGhg;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PowerBiMapper {
    @Mapping(target = "ghg", ignore = true)
    @Mapping(target = "typeName", source = "type.name")
    @Mapping(target = "buildingName", source = "building.name")
    @Mapping(target = "buildingGroupName", source = "buildingGroup.name")
    @Mapping(target = "tenantName", source = "buildingGroup.tenant.name")
    @Mapping(target = "factorNameVN", source = "emissionFactorEntity.nameVN")
    @Mapping(target = "factorNameEN", source = "emissionFactorEntity.nameEN")
    @Mapping(target = "factorNameZH", source = "emissionFactorEntity.nameZH")
    @Mapping(target = "factorValidFrom", source = "emissionFactorEntity.validFrom")
    @Mapping(target = "factorValidTo", source = "emissionFactorEntity.validTo")
    @Mapping(target = "sourceNameVN", source = "emissionFactorEntity.source.nameVN")
    @Mapping(target = "sourceNameEN", source = "emissionFactorEntity.source.nameEN")
    @Mapping(target = "sourceNameZH", source = "emissionFactorEntity.source.nameZH")
    EmissionActivityGhg toEmissionActivityGhg(EmissionActivityEntity source);
}