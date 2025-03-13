package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.dtos.FuelDTO;
import greenbuildings.enterprise.mappers.FuelMapper;
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
    private final FuelMapper fuelMapper;
    
    @GetMapping
    public List<FuelDTO> findAll() {
        return fuelService.findAll().stream().map(fuelMapper::toEntity).toList();
    }
    
}
