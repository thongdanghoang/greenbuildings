package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.dtos.ActivityTypeDTO;
import greenbuildings.enterprise.mappers.ActivityTypeMapper;
import greenbuildings.enterprise.services.impl.ActivityTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/activity-types")
@RequiredArgsConstructor
public class ActivityTypeController {
    
    private final ActivityTypeServiceImpl service;
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
}
