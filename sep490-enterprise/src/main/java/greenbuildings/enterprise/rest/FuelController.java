package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.enterprise.dtos.FuelCriteriaDTO;
import greenbuildings.enterprise.dtos.FuelDTO;
import greenbuildings.enterprise.mappers.FuelMapper;
import greenbuildings.enterprise.services.FuelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuel")
@RequiredArgsConstructor
public class FuelController {
    
    private final FuelService fuelService;
    private final FuelMapper fuelMapper;
    
    @GetMapping
    public List<FuelDTO> findAll() {
        return fuelService.findAll().stream().map(fuelMapper::toDTO).toList();
    }

    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<FuelDTO>> searchFuel(@RequestBody SearchCriteriaDTO<FuelCriteriaDTO> searchCriteria) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = fuelService.search(searchCriteria, pageable);
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        fuelMapper::toDTO));
    }
    
}
