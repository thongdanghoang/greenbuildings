package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.enterprise.dtos.FuelDTO;
import greenbuildings.enterprise.dtos.FuelCriteriaDTO;
import greenbuildings.enterprise.entities.FuelEntity;
import greenbuildings.enterprise.mappers.FuelMapper;
import greenbuildings.enterprise.services.FuelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<FuelDTO> findById(@PathVariable UUID id) {
        FuelDTO fuelDTO = fuelService.findById(id)
                .map(fuelMapper::toDTO)
                .orElseThrow();
        return ResponseEntity.ok(fuelDTO);
    }

    @PostMapping()
    public ResponseEntity<Void> createOrUpdateFuel(@RequestBody FuelDTO fuelDTO) {
        if (Objects.isNull(fuelDTO.id())) {
            return createNewFuel(fuelDTO);
        }

        return updateExistingFuel(fuelDTO)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<Void> createNewFuel(FuelDTO fuelDTO) {
        var fuelEntity = fuelMapper.createNewFuel(fuelDTO);
        return saveAndReturnResponse(fuelEntity, HttpStatus.CREATED);
    }

    private Optional<ResponseEntity<Void>> updateExistingFuel(FuelDTO fuelDTO) {
        return fuelService.findById(fuelDTO.id())
                .map(existingEntity -> {
                    var updatedEntity = fuelMapper.updateFuel(existingEntity, fuelDTO);
                    return saveAndReturnResponse(updatedEntity, HttpStatus.OK);
                });
    }


    private ResponseEntity<Void> saveAndReturnResponse(FuelEntity entity, HttpStatus status) {
        fuelService.createOrUpdate(entity);
        return ResponseEntity.status(status).build();
    }
    
}
