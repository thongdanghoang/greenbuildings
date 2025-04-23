package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.dto.PageDTO;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.TestcontainersConfigs;
import greenbuildings.enterprise.dtos.ActivityTypeCriteriaDTO;
import greenbuildings.enterprise.dtos.ActivityTypeDTO;
import greenbuildings.enterprise.dtos.EnterpriseDTO;
import greenbuildings.enterprise.mappers.EnterpriseMapper;
import greenbuildings.enterprise.services.EnterpriseService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class ActivityTypeControllerTest extends TestcontainersConfigs {
    
    @Autowired
    private EnterpriseService enterpriseService;
    
    @Autowired
    private EnterpriseMapper enterpriseMapper;
    
    private UUID createEnterprise() {
        var enterprise = EnterpriseDTO
                .builder()
                .email(randomEmail())
                .name(randomString())
                .hotline(randomNumber())
                .build();
        return enterpriseService.createEnterprise(
                enterpriseMapper.createEnterprise(enterprise)
                                                 );
    }
    
    @Test
    void createOrUpdate_returns201() {
        var payload = ActivityTypeDTO
                .builder()
                .name("Etiam vestibulum nulla sed magna.")
                .description("Donec iaculis viverra ante et.")
                .enterpriseId(createEnterprise())
                .build();
        RestAssured.given()
                   .auth().oauth2(getToken("enterprise.owner@greenbuildings.com", "enterprise.owner"))
                   .contentType(ContentType.JSON)
                   .when()
                   .body(payload)
                   .post("/activity-types/create")
                   .then()
                   .statusCode(201);
    }
    
    @Test
    void create_returns200() {
        var payload = ActivityTypeDTO
                .builder()
                .name("Etiam vestibulum nulla sed magna.")
                .description("Donec iaculis viverra ante et.")
                .enterpriseId(createEnterprise())
                .build();
        RestAssured.given()
                   .auth().oauth2(getToken("enterprise.owner@greenbuildings.com", "enterprise.owner"))
                   .contentType(ContentType.JSON)
                   .when()
                   .body(payload)
                   .post("/activity-types")
                   .then()
                   .statusCode(200);
    }
    
    @Test
    void create_invalidEnterpriseId_returns404() {
        var payload = ActivityTypeDTO
                .builder()
                .name("Nam eu faucibus nunc. Proin.")
                .description("Maecenas nisi sem, consequat et.")
                .enterpriseId(UUID.randomUUID())
                .build();
        RestAssured.given()
                   .auth().oauth2(getToken("enterprise.owner@greenbuildings.com", "enterprise.owner"))
                   .contentType(ContentType.JSON)
                   .when()
                   .body(payload)
                   .post("/activity-types")
                   .then()
                   .statusCode(404);
    }
    
    @Test
    void create_nullEnterpriseId_returns404() {
        var payload = ActivityTypeDTO
                .builder()
                .name("Donec vulputate sapien non elementum.")
                .description("In consequat suscipit nibh, eget.")
                .build();
        RestAssured.given()
                   .auth().oauth2(getToken("enterprise.owner@greenbuildings.com", "enterprise.owner"))
                   .contentType(ContentType.JSON)
                   .when()
                   .body(payload)
                   .post("/activity-types")
                   .then()
                   .statusCode(404);
    }
    
    @Test
    void searchActivityType_returns200() {
        RestAssured.given()
                   .auth().oauth2(getToken("enterprise.owner@greenbuildings.com", "enterprise.owner"))
                   .contentType(ContentType.JSON)
                   .body(new SearchCriteriaDTO<ActivityTypeCriteriaDTO>(PageDTO.of(10, 0), null, new ActivityTypeCriteriaDTO("Lorem Ipsum")))
                   .when()
                   .post("/activity-types/search")
                   .then()
                   .statusCode(200);
    }
    
    @Test
    void findAll_returns200() {
        RestAssured.given()
                   .auth().oauth2(getToken("enterprise.owner@greenbuildings.com", "enterprise.owner"))
                   .contentType(ContentType.JSON)
                   .when()
                   .get("/activity-types")
                   .then()
                   .statusCode(200);
    }
    
    @Test
    void findByEnterpriseId_returns404() {
        RestAssured.given()
                   .auth().oauth2(getToken("enterprise.owner@greenbuildings.com", "enterprise.owner"))
                   .contentType(ContentType.JSON)
                   .when()
                   .param("enterpriseId", UUID.randomUUID())
                   .get("/activity-types")
                   .then()
                   .statusCode(404);
    }
    
    @Test
    void findByEnterpriseId_returns200() {
        RestAssured.given()
                   .auth().oauth2(getToken("enterprise.owner@greenbuildings.com", "enterprise.owner"))
                   .contentType(ContentType.JSON)
                   .when()
                   .param("enterpriseId", createEnterprise())
                   .get("/activity-types")
                   .then()
                   .statusCode(200);
    }
    
    @Test
    void findById_returns404() {
        RestAssured.given()
                   .auth().oauth2(getToken("enterprise.owner@greenbuildings.com", "enterprise.owner"))
                   .contentType(ContentType.JSON)
                   .when()
                   .get("/activity-types/" + UUID.randomUUID())
                   .then()
                   .statusCode(404);
    }
    
    @Test
    void delete_returns417() {
        RestAssured.given()
                   .auth().oauth2(getToken("enterprise.owner@greenbuildings.com", "enterprise.owner"))
                   .contentType(ContentType.JSON)
                   .when()
                   .body(Collections.emptySet())
                   .delete("/activity-types")
                   .then()
                   .log().all()
                   .statusCode(417);
    }
    
    @Test
    void delete_returnsNoContent() {
        RestAssured.given()
                   .auth().oauth2(getToken("enterprise.owner@greenbuildings.com", "enterprise.owner"))
                   .contentType(ContentType.JSON)
                   .when()
                   .body(Set.of(UUID.randomUUID()))
                   .delete("/activity-types")
                   .then()
                   .log().all()
                   .statusCode(204);
    }
    
}
