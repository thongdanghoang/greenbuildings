package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.dtos.EmissionFactorDTO;
import greenbuildings.enterprise.mappers.EmissionFactorMapper;
import greenbuildings.enterprise.services.EmissionFactorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    
}
