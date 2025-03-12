package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.dtos.EmissionActivityDTO;
import greenbuildings.enterprise.services.EmissionActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emission-activity")
@RequiredArgsConstructor
public class EmissionActivityController {
    
    private final EmissionActivityService emissionActivityService;
    
    @PostMapping("")
    public void addEmissionActivity(EmissionActivityDTO dto) {
        // TODO
    }
    
}
