package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.TestcontainersConfigs;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.equalTo;

class DevControllerTest extends TestcontainersConfigs {
    
    @Test
    void saveBusinessErrorThrowsDemoException() {
        String body = "test body";
        RestAssured.given()
                   .contentType(ContentType.TEXT)
                   .auth().oauth2(getToken("system.admin@greenbuildings.com", "system.admin"))
                   .body(body)
                   .when()
                   .post("/dev/save-business-error")
                   .then()
                   .log().all()
                   .statusCode(HttpStatus.EXPECTATION_FAILED.value())
                   .body("field", equalTo("tenantEmail"))
                   .body("i18nKey", equalTo("demo"));
    }
    
    @Test
    void saveTechnicalErrorThrowsTechnicalException() {
        String body = "test body";
        RestAssured.given()
                   .contentType(ContentType.TEXT)
                   .auth().oauth2(getToken("system.admin@greenbuildings.com", "system.admin"))
                   .body(body)
                   .when()
                   .post("/dev/save-technical-error")
                   .then()
                   .log().all()
                   .statusCode(500)
                   .body("message", equalTo("Demo technical error"));
    }
}
