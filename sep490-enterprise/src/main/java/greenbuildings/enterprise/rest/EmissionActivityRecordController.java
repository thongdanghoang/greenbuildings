package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.enterprise.dtos.EmissionActivityRecordCriteria;
import greenbuildings.enterprise.dtos.EmissionActivityRecordDTO;
import greenbuildings.enterprise.dtos.NewEmissionActivityRecordDTO;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.entities.RecordFileEntity;
import greenbuildings.enterprise.mappers.EmissionActivityRecordMapper;
import greenbuildings.enterprise.services.EmissionActivityRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/emission-activity-record")
@RequiredArgsConstructor
public class EmissionActivityRecordController {
    
    private final EmissionActivityRecordService recordService;
    private final EmissionActivityRecordMapper recordMapper;
    
    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<EmissionActivityRecordDTO>> findAllByCriteria(
            @RequestBody SearchCriteriaDTO<EmissionActivityRecordCriteria> criteria) {
        Page<EmissionActivityRecordEntity> list = recordService.search(criteria);
        return ResponseEntity.ok(CommonMapper.toSearchResultDTO(list, recordMapper::toDTO));
    }
    
    @PostMapping
    public ResponseEntity<?> createRecord(@RequestPart("record") NewEmissionActivityRecordDTO recordDTO,
                                          @RequestPart(value = "file", required = false) MultipartFile file) {
        recordService.createWithFile(recordMapper.newToEntity(recordDTO), file);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping
    public ResponseEntity<Void> deleteActivities(@RequestBody Set<UUID> ids) {
        recordService.deleteRecords(ids);
        return ResponseEntity.noContent().build();
    }
    
//    @PostMapping("/{recordId}/file")
//    public ResponseEntity<RecordFileEntity> uploadFile(@PathVariable UUID recordId, @RequestParam("file") MultipartFile file) {
//        // TODO: return ENTITY ???
//        return ResponseEntity.ok(recordService.uploadFile(recordId, file));
//    }
    
    @DeleteMapping("/{recordId}/file/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable UUID recordId, @PathVariable UUID fileId) {
        recordService.deleteFile(recordId, fileId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{recordId}/file/{fileId}/url")
    public ResponseEntity<Map<String, String>> getFileUrl(@PathVariable UUID recordId, @PathVariable UUID fileId) {
        String fileUrl = recordService.getFileUrl(recordId, fileId);
        return ResponseEntity.ok(Map.of("url", fileUrl));
    }
    
//    @GetMapping("/{recordId}/file")
//    public ResponseEntity<List<RecordFileEntity>> getRecordFiles(@PathVariable UUID recordId) {
//        // TODO: return ENTITY ???
//        return ResponseEntity.ok(recordService.getRecordFiles(recordId));
//    }
}