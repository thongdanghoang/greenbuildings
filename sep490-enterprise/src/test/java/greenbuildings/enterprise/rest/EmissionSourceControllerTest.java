package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.dto.PageDTO;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.TestcontainersConfigs;
import greenbuildings.enterprise.dtos.EmissionSourceCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionSourceDTO;
import greenbuildings.enterprise.entities.EmissionSourceEntity;
import greenbuildings.enterprise.repositories.EmissionSourceRepository;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EmissionSourceControllerTest extends TestcontainersConfigs {
    
    @Autowired
    private EmissionSourceRepository emissionSourceRepository;
    
    @Override
    protected String getBaseUrl() {
        return "/emission-source";
    }
    
    @Test
    void findAll() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl())
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    void searchEmissionSource() {
        var searchCriteria = SearchCriteriaDTO
                .of(
                        PageDTO.of(10, 0),
                        null,
                        new EmissionSourceCriteriaDTO(StringUtils.EMPTY)
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
    void createEmissionSource() {
        var body = EmissionSourceDTO
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
        var emissionSourceEntity = new EmissionSourceEntity();
        emissionSourceRepository.save(emissionSourceEntity);
        
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "/{id}", emissionSourceEntity.getId())
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    void updateEmissionSource() {
        var emissionSourceEntity = new EmissionSourceEntity();
        emissionSourceRepository.save(emissionSourceEntity);
        var persisted = emissionSourceRepository.findById(emissionSourceEntity.getId()).orElseThrow();
        
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
    
    @Test
    void importExcel() {
        var resourceAsStream = Thread.currentThread()
                                     .getContextClassLoader()
                                     .getResourceAsStream("emission_sources.xlsx");
        asEnterpriseOwner()
                .when()
                .multiPart("file", "emission_sources.xlsx", resourceAsStream, excelMimeType())
                .post(getBaseUrl() + "/excel")
                .then()
                .log().all()
                .statusCode(201);
    }
    
    
}
