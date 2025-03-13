package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.dtos.EmissionSourceDTO;
import greenbuildings.enterprise.mappers.EmissionSourceMapper;
import greenbuildings.enterprise.services.EmissionSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
    
}
