package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.ChemicalDensityDTO;
import greenbuildings.enterprise.entities.ChemicalDensityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChemicalDensityMapper {
    ChemicalDensityDTO toDTO(ChemicalDensityEntity chemicalDensityEntity);

    @Mapping(target = "id", ignore = true)
    ChemicalDensityEntity createNewChemicalDensity(ChemicalDensityDTO dto);

    @Mapping(target = "id", ignore = true)
    ChemicalDensityEntity updateChemicalDensity(@MappingTarget ChemicalDensityEntity entity, ChemicalDensityDTO dto);
}
