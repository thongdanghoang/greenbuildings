package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.TestcontainersConfigs;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.equalTo;

class DevControllerTest extends TestcontainersConfigs {
    
    @Override
    protected String getBaseUrl() {
        return "/dev";
    }
    
    @Test
    void saveBusinessErrorThrowsDemoException() {
        String body = "test body";
        asSystemAdmin()
                .contentType(ContentType.TEXT)
                .body(body)
                .when()
                .post(getBaseUrl() + "/save-business-error")
                .then()
                .log().all()
                .statusCode(HttpStatus.EXPECTATION_FAILED.value())
                .body("field", equalTo("tenantEmail"))
                .body("i18nKey", equalTo("demo"));
    }
    
    @Test
    void saveTechnicalErrorThrowsTechnicalException() {
        String body = "test body";
        asSystemAdmin()
                .contentType(ContentType.TEXT)
                .body(body)
                .when()
                .post(getBaseUrl() + "/save-technical-error")
                .then()
                .log().all()
                .statusCode(500)
                .body("message", equalTo("Demo technical error"));
    }
}
