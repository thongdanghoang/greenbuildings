package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.dto.PageDTO;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.TestcontainersConfigs;
import greenbuildings.enterprise.dtos.EnergyConversionDTO;
import greenbuildings.enterprise.dtos.FuelCriteriaDTO;
import greenbuildings.enterprise.dtos.FuelDTO;
import greenbuildings.enterprise.entities.EnergyConversionEntity;
import greenbuildings.enterprise.entities.FuelEntity;
import greenbuildings.enterprise.repositories.EnergyConversionRepository;
import greenbuildings.enterprise.repositories.FuelRepository;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EnergyConversionControllerTest extends TestcontainersConfigs {
    
    @Override
    protected String getBaseUrl() {
        return "/energy-conversion";
    }
    
    @Autowired
    private EnergyConversionRepository energyConversionRepository;
    
    @Autowired
    private FuelRepository fuelRepository;
    
    @Test
    void searchFuel() {
        var searchCriteria = SearchCriteriaDTO
                .of(
                        PageDTO.of(10, 0),
                        null,
                        new FuelCriteriaDTO(StringUtils.EMPTY)
                   );
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(searchCriteria)
                .post(getBaseUrl() + "/search")
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    void createEnergyConversion() {
        var body = EnergyConversionDTO
                .builder()
                .fuel(FuelDTO.builder()
                             .nameEN(randomString())
                             .nameVN(randomString())
                             .nameZH(randomString())
                             .build())
                .build();
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .post(getBaseUrl())
                .then()
                .log().all()
                .statusCode(201);
    }
    
    @Test
    void findById() {
        var conversion = new EnergyConversionEntity();
        energyConversionRepository.save(conversion);
        
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "/{id}", conversion.getId())
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    void updateEmissionSource() {
        var fuel = new FuelEntity();
        fuel.setNameEN(randomString());
        fuel.setNameVN(randomString());
        fuel.setNameZH(randomString());
        var conversion = new EnergyConversionEntity();
        conversion.setFuel(fuelRepository.save(fuel));
        energyConversionRepository.save(conversion);
        var persisted = energyConversionRepository.findById(conversion.getId()).orElseThrow();
        
        var body = EnergyConversionDTO
                .builder()
                .id(persisted.getId())
                .version(persisted.getVersion())
                .fuel(FuelDTO.builder().id(fuel.getId()).build())
                .build();
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .post(getBaseUrl())
                .then()
                .log().all()
                .statusCode(200);
    }
}
