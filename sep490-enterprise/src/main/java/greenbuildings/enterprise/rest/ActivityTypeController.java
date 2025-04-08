package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.enterprise.dtos.ActivityTypeCriteriaDTO;
import greenbuildings.enterprise.dtos.ActivityTypeDTO;
import greenbuildings.enterprise.dtos.EmissionSourceCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionSourceDTO;
import greenbuildings.enterprise.entities.ActivityTypeEntity;
import greenbuildings.enterprise.entities.EmissionSourceEntity;
import greenbuildings.enterprise.mappers.ActivityTypeMapper;
import greenbuildings.enterprise.services.ActivityTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/activity-types")
@RequiredArgsConstructor
public class ActivityTypeController {
    
    private final ActivityTypeService service;
    private final ActivityTypeMapper mapper;
    
    @GetMapping
    public ResponseEntity<List<ActivityTypeDTO>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toDTO).toList());
    }
    
    @GetMapping(params = "enterpriseId")
    public ResponseEntity<List<ActivityTypeDTO>> findByBuildingId(@RequestParam UUID enterpriseId) {
        return ResponseEntity.ok(service.findByEnterpriseId(enterpriseId).stream().map(mapper::toDTO).toList());
    }
    
    @PostMapping
    public ResponseEntity<ActivityTypeDTO> create(@RequestBody ActivityTypeDTO dto) {
        return ResponseEntity.ok(mapper.toDTO(service.create(dto)));
    }

    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<ActivityTypeDTO>> searchActivityType(@RequestBody SearchCriteriaDTO<ActivityTypeCriteriaDTO> searchCriteria) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = service.search(searchCriteria, pageable);
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        mapper::toDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityTypeDTO> findById(@PathVariable UUID id) {
        ActivityTypeDTO activityTypeDTO = service.findById(id)
                .map(mapper::toDTO)
                .orElseThrow();
        return ResponseEntity.ok(activityTypeDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createOrUpdate(@RequestBody ActivityTypeDTO dto) {
        if (Objects.isNull(dto.id())) {
            return createNew(dto);
        }

        return updateExisting(dto)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<Void> createNew(ActivityTypeDTO activityTypeDTO) {
        var emissionSourceEntity = mapper.createEntity(activityTypeDTO);
        return saveAndReturnResponse(emissionSourceEntity, HttpStatus.CREATED);
    }

    private Optional<ResponseEntity<Void>> updateExisting(ActivityTypeDTO activityTypeDTO) {
        return service.findById(activityTypeDTO.id())
                .map(existingEntity -> {
                    var updatedEntity = mapper.updateEntity(activityTypeDTO, existingEntity);
                    return saveAndReturnResponse(updatedEntity, HttpStatus.OK);
                });
    }

    private ResponseEntity<Void> saveAndReturnResponse(ActivityTypeEntity entity, HttpStatus status) {
        service.createOrUpdate(entity);
        return ResponseEntity.status(status).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Set<UUID> typeIds) {
        service.delete(typeIds);
        return ResponseEntity.noContent().build();
    }

}
