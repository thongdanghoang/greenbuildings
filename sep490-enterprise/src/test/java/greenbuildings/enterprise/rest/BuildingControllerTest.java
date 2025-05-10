package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.TestcontainersConfigs;
import greenbuildings.enterprise.dtos.BuildingDTO;
import greenbuildings.enterprise.dtos.DownloadReportDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class BuildingControllerTest extends TestcontainersConfigs {
    
    @Override
    protected String getBaseUrl() {
        return "/buildings";
    }
    
    @Test
    void findById() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "/" + insertBuildingEntity().getId())
                .then()
                .statusCode(200);
    }
    
    @Test
    void selectable() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "/selectable")
                .then()
                .statusCode(200);
    }
    
    @Test
    void getEnterpriseBuildings_withValidToken_returns200() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .body(new SearchCriteriaDTO<Void>(null, null, null))
                .when()
                .post(getBaseUrl() + "/search")
                .then()
                .statusCode(200);
    }
    
    @Test
    void getEnterpriseBuildings_withInvalidToken_returns401() {
        RestAssured.given()
                   .auth().oauth2("invalid_token")
                   .contentType(ContentType.JSON)
                   .body(new SearchCriteriaDTO<Void>(null, null, null))
                   .when()
                   .post(getBaseUrl() + "/search")
                   .then()
                   .statusCode(401);
    }
    
    @Test
    void createBuilding_withValidToken_returns201() {
        var building = BuildingDTO.builder()
                                  .name("Building 1")
                                  .area(100.0)
                                  .numberOfLevels(5)
                                  .address("FPT University - HCMC Campus, Lô E2a-7, Đường D1, Long Thanh My Ward, Thủ Đức, Ho Chi Minh City, 00848, Vietnam")
                                  .build();
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .body(building)
                .when()
                .post(getBaseUrl())
                .then()
                .statusCode(201);
    }
    
    @Test
    void createBuilding_withMissingFields_returns400() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .body(BuildingDTO.builder().build())
                .when()
                .post(getBaseUrl())
                .then()
                .statusCode(400);
    }
    
    @Test
    void createBuilding_withInvalidToken_returns401() {
        RestAssured.given()
                   .auth().oauth2("invalid_token")
                   .contentType(ContentType.JSON)
                   .body(BuildingDTO.builder().build())
                   .when()
                   .post(getBaseUrl())
                   .then()
                   .statusCode(401);
    }
    
    @Test
    void generateReport() {
        // given
        var buildingEntity = insertBuildingEntity();
        var group1 = insertBuildingGroupEntity(buildingEntity);
        var group2 = insertBuildingGroupEntity(buildingEntity);
        var emissionActivityDirect = insertActivity(true);
        var emissionActivityIndirect = insertActivity(false);
        var startDate = LocalDate.now().minusMonths(60);
        for (int i = 0; i < 10; i++) {
            var endDate = startDate.plusMonths(i);
            insertRecord(emissionActivityDirect, startDate, endDate);
            startDate = endDate.plusDays(1);
        }
        for (int i = 0; i < 10; i++) {
            var endDate = startDate.plusMonths(i);
            insertRecord(emissionActivityIndirect, startDate, endDate);
            startDate = endDate.plusDays(1);
        }
        var downloadReport = DownloadReportDTO
                .builder()
                .buildingID(buildingEntity.getId())
                .startDate(startDate)
                .endDate(LocalDate.now())
                .selectedGroups(List.of(group1.getId(), group2.getId()))
                .build();
        // when
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .body(downloadReport)
                .when()
                .post(getBaseUrl() + "/generate-report")
                .then()
                .log().all()
                .statusCode(200)
                .contentType("application/octet-stream");
    }
}
