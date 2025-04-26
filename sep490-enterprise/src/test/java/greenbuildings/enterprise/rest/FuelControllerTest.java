package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.dto.PageDTO;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.TestcontainersConfigs;
import greenbuildings.enterprise.dtos.EmissionSourceDTO;
import greenbuildings.enterprise.dtos.FuelCriteriaDTO;
import greenbuildings.enterprise.dtos.FuelDTO;
import greenbuildings.enterprise.entities.FuelEntity;
import greenbuildings.enterprise.repositories.FuelRepository;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FuelControllerTest extends TestcontainersConfigs {
    
    @Autowired
    private FuelRepository fuelRepository;
    
    @Override
    protected String getBaseUrl() {
        return "/fuel";
    }
    
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
    void createNewFuel() {
        var body = FuelDTO
                .builder()
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
        var fuel = new FuelEntity();
        fuelRepository.save(fuel);
        
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "/{id}", fuel.getId())
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    void updateEmissionSource() {
        var fuel = new FuelEntity();
        fuelRepository.save(fuel);
        var persisted = fuelRepository.findById(fuel.getId()).orElseThrow();
        
        var body = EmissionSourceDTO
                .builder()
                .id(persisted.getId())
                .version(persisted.getVersion())
                .nameVN(randomString())
                .nameEN(randomString())
                .nameZH(randomString())
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
