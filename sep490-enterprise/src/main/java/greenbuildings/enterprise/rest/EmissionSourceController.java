package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.enterprise.dtos.EmissionSourceCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionSourceDTO;
import greenbuildings.enterprise.mappers.EmissionSourceMapper;
import greenbuildings.enterprise.services.EmissionSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
