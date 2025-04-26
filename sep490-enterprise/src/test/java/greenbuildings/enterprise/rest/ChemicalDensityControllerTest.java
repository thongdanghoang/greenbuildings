package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.dto.PageDTO;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.TestcontainersConfigs;
import greenbuildings.enterprise.dtos.ChemicalDensityCriteria;
import greenbuildings.enterprise.dtos.ChemicalDensityDTO;
import greenbuildings.enterprise.entities.ChemicalDensityEntity;
import greenbuildings.enterprise.enums.EmissionUnit;
import greenbuildings.enterprise.repositories.ChemicalDensityRepository;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ChemicalDensityControllerTest extends TestcontainersConfigs {
    @Override
    protected String getBaseUrl() {
        return "/chemical-density";
    }
    
    @Autowired
    private ChemicalDensityRepository chemicalDensityRepository;
    
    @Test
    void searchFuel() {
        var searchCriteria = SearchCriteriaDTO
                .of(
                        PageDTO.of(10, 0),
                        null,
                        new ChemicalDensityCriteria(StringUtils.EMPTY)
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
    void createChemicalDensity() {
        var body = ChemicalDensityDTO
                .builder()
                .chemicalFormula("CO2")
                .value(1.977)
                .unitNumerator(EmissionUnit.KILOGRAM)
                .unitDenominator(EmissionUnit.CUBIC_METER)
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
        var density = new ChemicalDensityEntity();
        density.setChemicalFormula("CO2");
        density.setValue(1.977);
        density.setUnitNumerator(EmissionUnit.KILOGRAM);
        density.setUnitDenominator(EmissionUnit.CUBIC_METER);
        chemicalDensityRepository.save(density);
        
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "/{id}", density.getId())
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    void updateEmissionSource() {
        var density = new ChemicalDensityEntity();
        density.setChemicalFormula("CH4");
        density.setValue(2.977);
        density.setUnitNumerator(EmissionUnit.KILOGRAM);
        density.setUnitDenominator(EmissionUnit.CUBIC_METER);
        chemicalDensityRepository.save(density);
        var densityEntity = chemicalDensityRepository.findById(density.getId()).orElseThrow();
        
        var body = ChemicalDensityDTO
                .builder()
                .id(densityEntity.getId())
                .version(densityEntity.getVersion())
                .chemicalFormula("CH4")
                .value(4.977)
                .unitNumerator(EmissionUnit.GIGAGRAM)
                .unitDenominator(EmissionUnit.CUBIC_METER)
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
