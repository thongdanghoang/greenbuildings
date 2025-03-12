package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.entities.EmissionSourceEntity;
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
    
    @GetMapping
    public List<EmissionSourceEntity> findAll() {
        return emissionSourceService.findAll();
    }
    
    
}
