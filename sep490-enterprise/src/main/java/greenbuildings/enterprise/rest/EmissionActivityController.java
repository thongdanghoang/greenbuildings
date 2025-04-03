package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.enterprise.dtos.EmissionActivityCriteria;
import greenbuildings.enterprise.dtos.EmissionActivityDTO;
import greenbuildings.enterprise.dtos.EmissionActivityDetailsDTO;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.mappers.EmissionActivityMapper;
import greenbuildings.enterprise.services.EmissionActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/emission-activity")
@RequiredArgsConstructor
public class EmissionActivityController {
    
    private final EmissionActivityService emissionActivityService;
    private final EmissionActivityMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<EmissionActivityDetailsDTO> getEmissionActivityDetails(@PathVariable UUID id) {
        EmissionActivityEntity entity = emissionActivityService.getEmissionActivityDetails(id);
        return ResponseEntity.ok(mapper.toDetailsDTO(entity));
    }
    
    @PostMapping("/building")
    public ResponseEntity<SearchResultDTO<EmissionActivityDTO>> findAllByCriteria(@RequestBody SearchCriteriaDTO<EmissionActivityCriteria> criteria) {
        Page<EmissionActivityEntity> list = emissionActivityService.search(criteria);
        return ResponseEntity.ok(CommonMapper.toSearchResultDTO(list, mapper::toDTO));
    }

    @PostMapping
    public ResponseEntity<EmissionActivityDetailsDTO> addOrUpdateEmissionActivity(@RequestBody EmissionActivityDTO dto) {
        EmissionActivityEntity entity;
        if (dto.id() != null) {
            entity = this.mapper.updateActivity(dto);
        } else {
            entity = this.mapper.createNewActivity(dto);
        }
        return ResponseEntity.ok(mapper.toDetailsDTO(emissionActivityService.addOrUpdate(entity)));
    }
    
    @DeleteMapping
    public ResponseEntity<Void> deleteActivities(@RequestBody Set<UUID> ids) {
        emissionActivityService.deleteActivities(ids);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/building/{id}")
    public ResponseEntity<List<EmissionActivityDTO>> getAllActivitiesByBuildingId(@PathVariable UUID id) {
        List<EmissionActivityEntity> list = emissionActivityService.getAllActivitiesByBuildingId(id);
        return ResponseEntity.ok(list.stream().map(mapper::toDTO).toList());
    }
}
