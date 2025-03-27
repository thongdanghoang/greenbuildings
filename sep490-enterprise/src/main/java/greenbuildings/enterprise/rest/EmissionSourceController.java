package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.enterprise.dtos.EmissionSourceCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionSourceDTO;
import greenbuildings.enterprise.entities.EmissionSourceEntity;
import greenbuildings.enterprise.mappers.EmissionSourceMapper;
import greenbuildings.enterprise.services.EmissionSourceService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/emission-source")
@RequiredArgsConstructor
public class EmissionSourceController {
    
    private final EmissionSourceService emissionSourceService;
    private final EmissionSourceMapper emissionSourceMapper;
    
    @GetMapping
    public List<EmissionSourceDTO> findAll() {
        return emissionSourceService.findAll().stream().map(emissionSourceMapper::toDTO).toList();
    }

    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<EmissionSourceDTO>> searchEmissionSource(@RequestBody SearchCriteriaDTO<EmissionSourceCriteriaDTO> searchCriteria) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = emissionSourceService.search(searchCriteria, pageable);
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        emissionSourceMapper::toDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmissionSourceDTO> findById(@PathVariable UUID id) {
        EmissionSourceDTO emissionSourceDTO = emissionSourceService.findById(id)
                .map(emissionSourceMapper::toDTO)
                .orElseThrow();
        return ResponseEntity.ok(emissionSourceDTO);
    }

    @PostMapping()
    public ResponseEntity<Void> createOrUpdateEmissionSource(@RequestBody EmissionSourceDTO emissionSourceDTO) {
        if (Objects.isNull(emissionSourceDTO.id())) {
            return createNewEmissionSource(emissionSourceDTO);
        }

        return updateExistingEmissionSource(emissionSourceDTO)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<Void> createNewEmissionSource(EmissionSourceDTO emissionSourceDTO) {
        var emissionSourceEntity = emissionSourceMapper.createNewEmissionSource(emissionSourceDTO);
        return saveAndReturnResponse(emissionSourceEntity, HttpStatus.CREATED);
    }

    private Optional<ResponseEntity<Void>> updateExistingEmissionSource(EmissionSourceDTO emissionSourceDTO) {
        return emissionSourceService.findById(emissionSourceDTO.id())
                .map(existingEntity -> {
                    var updatedEntity = emissionSourceMapper.updateEmissionSource(existingEntity, emissionSourceDTO);
                    return saveAndReturnResponse(updatedEntity, HttpStatus.OK);
                });
    }


    private ResponseEntity<Void> saveAndReturnResponse(EmissionSourceEntity entity, HttpStatus status) {
        emissionSourceService.createOrUpdate(entity);
        return ResponseEntity.status(status).build();
    }
}
