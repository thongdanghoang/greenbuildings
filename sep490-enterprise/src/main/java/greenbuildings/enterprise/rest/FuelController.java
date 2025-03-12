package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.entities.FuelEntity;
import greenbuildings.enterprise.repositories.FuelRepository;
import greenbuildings.enterprise.services.FuelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fuel")
@RequiredArgsConstructor
public class FuelController {
    
    private final FuelService fuelService;
    
    @GetMapping
    public List<FuelEntity> findAll() {
        return fuelService.findAll();
    }
    
}
