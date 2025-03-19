package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.enterprise.dtos.EmissionActivityCriteria;
import greenbuildings.enterprise.dtos.EmissionActivityDTO;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.mappers.EmissionActivityMapper;
import greenbuildings.enterprise.services.EmissionActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emission-activity")
@RequiredArgsConstructor
public class EmissionActivityController {
    
    private final EmissionActivityService emissionActivityService;
    private final EmissionActivityMapper mapper;

    @PostMapping("/building")
    public ResponseEntity<SearchResultDTO<EmissionActivityDTO>> findAllByCriteria(@RequestBody SearchCriteriaDTO<EmissionActivityCriteria> criteria) {
        Page<EmissionActivityEntity> list = emissionActivityService.search(criteria);
        return ResponseEntity.ok(CommonMapper.toSearchResultDTO(list, mapper::toDTO));
    }

    @PostMapping
    public ResponseEntity<EmissionActivityDTO> addEmissionActivity(@RequestBody EmissionActivityDTO dto) {
        EmissionActivityEntity entity = this.mapper.createNewActivity(dto);
        return ResponseEntity.ok(mapper.toDTO(emissionActivityService.add(entity)));
    }
    
}
