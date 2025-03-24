package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.enterprise.dtos.EmissionActivityRecordCriteria;
import greenbuildings.enterprise.dtos.EmissionActivityRecordDTO;
import greenbuildings.enterprise.dtos.NewEmissionActivityRecordDTO;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.mappers.EmissionActivityRecordMapper;
import greenbuildings.enterprise.services.EmissionActivityRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emission-activity-record")
@RequiredArgsConstructor
public class EmissionActivityRecordController {
    
    private final EmissionActivityRecordService recordService;
    private final EmissionActivityRecordMapper recordMapper;

    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<EmissionActivityRecordDTO>> findAllByCriteria(@RequestBody SearchCriteriaDTO<EmissionActivityRecordCriteria> criteria) {
        Page<EmissionActivityRecordEntity> list = recordService.search(criteria);
        return ResponseEntity.ok(CommonMapper.toSearchResultDTO(list, recordMapper::toDTO));
    }

    @PostMapping
    public ResponseEntity<EmissionActivityRecordDTO> addOrUpdateRecord(@RequestBody NewEmissionActivityRecordDTO dto) {
        EmissionActivityRecordEntity entity = recordService.addOrUpdate(recordMapper.newToEntity(dto));
        return ResponseEntity.ok(recordMapper.toDTO(entity));
    }
} 