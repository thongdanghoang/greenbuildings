package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.enterprise.dtos.EmissionFactorCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionFactorDTO;
import greenbuildings.enterprise.mappers.EmissionFactorMapper;
import greenbuildings.enterprise.services.EmissionFactorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/emission-factor")
@RequiredArgsConstructor
public class EmissionFactorController {
    
    private final EmissionFactorService emissionFactorService;
    private final EmissionFactorMapper emissionFactorMapper;
    
    @GetMapping
    public ResponseEntity<List<EmissionFactorDTO>> findAll() {
        return ResponseEntity.ok(emissionFactorService.findAll().stream().map(emissionFactorMapper::toDTO).toList());
    }

    @GetMapping("/find-by-source/{sourceId}")
    public List<EmissionFactorDTO> findBySource(@PathVariable UUID sourceId) {
        return emissionFactorService.findBySource(sourceId).stream().map(emissionFactorMapper::toDTO).toList();
    }

    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<EmissionFactorDTO>> searchEmissionFactor(@RequestBody SearchCriteriaDTO<EmissionFactorCriteriaDTO> searchCriteria) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = emissionFactorService.search(searchCriteria, pageable);
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        emissionFactorMapper::toDTO));
    }
}
