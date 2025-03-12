package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.entities.EmissionFactorEntity;
import greenbuildings.enterprise.services.EmissionFactorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/emission-factor")
@RequiredArgsConstructor
public class EmissionFactorController {
    
    private final EmissionFactorService emissionFactorService;
    
    @GetMapping
    public List<EmissionFactorEntity> findAll() {
        return emissionFactorService.findAll();
    }
}
