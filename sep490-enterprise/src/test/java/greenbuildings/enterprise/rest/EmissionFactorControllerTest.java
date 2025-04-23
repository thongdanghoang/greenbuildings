package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.TestcontainersConfigs;
import greenbuildings.enterprise.dtos.EmissionFactorDTO;
import greenbuildings.enterprise.dtos.EmissionSourceDTO;
import greenbuildings.enterprise.entities.EmissionFactorEntity;
import greenbuildings.enterprise.entities.EmissionSourceEntity;
import greenbuildings.enterprise.enums.EmissionUnit;
import greenbuildings.enterprise.repositories.EmissionFactorRepository;
import greenbuildings.enterprise.repositories.EmissionSourceRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmissionFactorControllerTest extends TestcontainersConfigs {
    
    @Autowired
    private EmissionSourceRepository emissionSourceRepository;
    
    @Autowired
    private EmissionFactorRepository emissionFactorRepository;
    
    @Override
    protected String getBaseUrl() {
        return "/emission-factor";
    }
    
    private EmissionSourceEntity createEmissionSource() {
        var emissionSource = new EmissionSourceEntity();
        emissionSource.setNameEN("Duis facilisis vestibulum felis non euismod.");
        emissionSource.setNameVN("Quisque ultricies lorem ante, in ornare arcu euismod eu.");
        emissionSource.setNameZH("测试排放源");
        return emissionSourceRepository.save(emissionSource);
    }
    
    private EmissionFactorEntity createEmissionFactor() {
        var emissionFactor = new EmissionFactorEntity();
        emissionFactor.setEmissionUnitNumerator(EmissionUnit.KILOGRAM);
        emissionFactor.setEmissionUnitDenominator(EmissionUnit.TERAJOULE);
        emissionFactor.setValidFrom(LocalDateTime.now().minusYears(1));
        emissionFactor.setValidTo(LocalDateTime.now().plusYears(1));
        emissionFactor.setSource(createEmissionSource());
        emissionFactor.setActive(true);
        return emissionFactorRepository.save(emissionFactor);
    }
    
    @Test
    void createOrEmissionFactor_returns201() {
        var payload = EmissionFactorDTO
                .builder()
                .emissionUnitNumerator(EmissionUnit.KILOGRAM)
                .emissionUnitDenominator(EmissionUnit.TERAJOULE)
                .validFrom(LocalDate.now().minusYears(1))
                .validTo(LocalDate.now().plusYears(1))
                .emissionSourceDTO(EmissionSourceDTO.builder().id(createEmissionSource().getId()).build())
                .build();
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(payload)
                .post(getBaseUrl())
                .then()
                .statusCode(201);
    }
    
    @Test
    void createOrEmissionFactor_returns200() {
        var existEmissionFactor = createEmissionFactor();
        var payload = EmissionFactorDTO
                .builder()
                .id(existEmissionFactor.getId())
                .version(existEmissionFactor.getVersion())
                .emissionUnitNumerator(EmissionUnit.KILOGRAM)
                .emissionUnitDenominator(EmissionUnit.TERAJOULE)
                .validFrom(LocalDate.now().minusYears(1))
                .validTo(LocalDate.now().plusYears(1))
                .emissionSourceDTO(EmissionSourceDTO.builder().id(createEmissionSource().getId()).build())
                .build();
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(payload)
                .post(getBaseUrl())
                .then()
                .statusCode(200);
    }
    
    @Test
    void findById_returns200() {
        var existEmissionFactor = createEmissionFactor();
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "/" + existEmissionFactor.getId())
                .then()
                .statusCode(200);
    }
    
}
