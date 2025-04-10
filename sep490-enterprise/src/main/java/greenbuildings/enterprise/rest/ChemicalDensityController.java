package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.enterprise.dtos.ChemicalDensityCriteria;
import greenbuildings.enterprise.dtos.ChemicalDensityDTO;
import greenbuildings.enterprise.dtos.EmissionSourceCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionSourceDTO;
import greenbuildings.enterprise.entities.ChemicalDensityEntity;
import greenbuildings.enterprise.entities.EmissionSourceEntity;
import greenbuildings.enterprise.mappers.ChemicalDensityMapper;
import greenbuildings.enterprise.services.ChemicalDensityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/chemical-density")
@RequiredArgsConstructor
public class ChemicalDensityController {
    private final ChemicalDensityService chemicalDensityService;
    private final ChemicalDensityMapper chemicalDensityMapper;
    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<ChemicalDensityDTO>> searchEmissionSource(@RequestBody SearchCriteriaDTO<ChemicalDensityCriteria> searchCriteria) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = chemicalDensityService.search(searchCriteria, pageable);
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        chemicalDensityMapper::toDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChemicalDensityDTO> findById(@PathVariable UUID id) {
        ChemicalDensityDTO chemicalDensityDTO = chemicalDensityService.findById(id)
                .map(chemicalDensityMapper::toDTO)
                .orElseThrow();
        return ResponseEntity.ok(chemicalDensityDTO);
    }

    @PostMapping()
    public ResponseEntity<Void> createOrUpdate(@RequestBody ChemicalDensityDTO chemicalDensityDTO) {
        if (Objects.isNull(chemicalDensityDTO.id())) {
            return createNewEmissionSource(chemicalDensityDTO);
        }

        return updateExistingEmissionSource(chemicalDensityDTO)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<Void> createNewEmissionSource(ChemicalDensityDTO emissionSourceDTO) {
        var emissionSourceEntity = chemicalDensityMapper.createNewChemicalDensity(emissionSourceDTO);
        return saveAndReturnResponse(emissionSourceEntity, HttpStatus.CREATED);
    }

    private Optional<ResponseEntity<Void>> updateExistingEmissionSource(ChemicalDensityDTO emissionSourceDTO) {
        return chemicalDensityService.findById(emissionSourceDTO.id())
                .map(existingEntity -> {
                    var updatedEntity = chemicalDensityMapper.updateChemicalDensity(existingEntity, emissionSourceDTO);
                    return saveAndReturnResponse(updatedEntity, HttpStatus.OK);
                });
    }


    private ResponseEntity<Void> saveAndReturnResponse(ChemicalDensityEntity entity, HttpStatus status) {
        chemicalDensityService.createOrUpdate(entity);
        return ResponseEntity.status(status).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Set<UUID> chemicalIds) {
        chemicalDensityService.delete(chemicalIds);
        return ResponseEntity.noContent().build();
    }
}
