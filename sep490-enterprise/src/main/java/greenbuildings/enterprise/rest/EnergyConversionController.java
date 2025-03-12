package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.dtos.EnergyConversionDTO;
import greenbuildings.enterprise.mappers.EnergyConversionMapper;
import greenbuildings.enterprise.services.EnergyConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/energy-conversion")
@RequiredArgsConstructor
public class EnergyConversionController {
    
    private final EnergyConversionService energyConversionService;
    private final EnergyConversionMapper energyConversionMapper;
    
    @GetMapping
    public List<EnergyConversionDTO> findAll() {
        return energyConversionService.findAll().stream().map(energyConversionMapper::toDTO).toList();
    }
}
