package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.dto.PageDTO;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.TestcontainersConfigs;
import greenbuildings.enterprise.dtos.CreditPackageDTO;
import greenbuildings.enterprise.entities.CreditPackageEntity;
import greenbuildings.enterprise.entities.CreditPackageVersionEntity;
import greenbuildings.enterprise.repositories.CreditPackageRepository;
import greenbuildings.enterprise.repositories.CreditPackageVersionRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;
import java.util.stream.Collectors;

public class CreditPackageControllerTest extends TestcontainersConfigs {
    
    @Override
    protected String getBaseUrl() {
        return "/credit-package";
    }
    
    @Autowired
    private CreditPackageVersionRepository creditPackageVersionRepository;
    
    @Autowired
    private CreditPackageRepository creditPackageRepository;
    
    @Test
    void findAll_returns200() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl())
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    void findById_returns404() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "/" + UUID.randomUUID())
                .then()
                .log().all()
                .statusCode(404);
    }
    
    @Test
    void findById_returns200() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "/" + creditPackageVersionRepository.findAll().stream().findAny().orElseThrow().getId())
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    void createCreditPackage_returns201() {
        var payload = CreditPackageDTO
                .builder()
                .numberOfCredits(Integer.parseInt(RandomStringUtils.randomNumeric(2, 3)))
                .price(Integer.parseInt(RandomStringUtils.randomNumeric(6, 9)))
                .discount(Integer.parseInt(RandomStringUtils.randomNumeric(1, 2)))
                .build();
        asSystemAdmin()
                .contentType(ContentType.JSON)
                .when()
                .body(payload)
                .post(getBaseUrl())
                .then()
                .log().all()
                .statusCode(201);
    }
    
    @Test
    void createCreditPackage_returns200() {
        var creditPackageEntity = new CreditPackageEntity();
        creditPackageEntity.setActive(true);
        var creditPackageVersionEntity = new CreditPackageVersionEntity();
        creditPackageVersionEntity.setActive(true);
        creditPackageVersionEntity.setDiscount(Integer.parseInt(RandomStringUtils.randomNumeric(1, 2)));
        creditPackageVersionEntity.setPrice(Integer.parseInt(RandomStringUtils.randomNumeric(6, 9)));
        creditPackageVersionEntity.setCreditPackageEntity(creditPackageRepository.save(creditPackageEntity));
        var packageVersionEntity = creditPackageVersionRepository.save(creditPackageVersionEntity);
        var payload = CreditPackageDTO
                .builder()
                .id(packageVersionEntity.getId())
                .version(packageVersionEntity.getVersion())
                .numberOfCredits(Integer.parseInt(RandomStringUtils.randomNumeric(2, 3)))
                .price(Integer.parseInt(RandomStringUtils.randomNumeric(6, 9)))
                .discount(Integer.parseInt(RandomStringUtils.randomNumeric(1, 2)))
                .build();
        asSystemAdmin()
                .contentType(ContentType.JSON)
                .when()
                .body(payload)
                .post(getBaseUrl())
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    void search() {
        asSystemAdmin()
                .contentType(ContentType.JSON)
                .when()
                .body(SearchCriteriaDTO.of(PageDTO.of(10, 0), null, null))
                .post(getBaseUrl() + "/search")
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    void deleteCreditPackages() {
        asSystemAdmin()
                .contentType(ContentType.JSON)
                .when()
                .body(creditPackageVersionRepository.findAllByActiveIsTrue().stream().map(CreditPackageVersionEntity::getId).collect(Collectors.toSet()))
                .delete(getBaseUrl())
                .then()
                .log().all()
                .statusCode(204);
    }
}
