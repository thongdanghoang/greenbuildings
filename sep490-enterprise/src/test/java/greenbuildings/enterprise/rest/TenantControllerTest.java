package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.dto.PageDTO;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.TestcontainersConfigs;
import greenbuildings.enterprise.dtos.TenantDTO;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

public class TenantControllerTest extends TestcontainersConfigs {
    @Override
    protected String getBaseUrl() {
        return "/tenants";
    }
    
    @Test
    public void findById() {
        var tenant = insertTenantEntity();
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "/{id}", tenant.getId())
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    public void findAll() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl())
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    public void search() {
        var criteria = SearchCriteriaDTO.of(
                PageDTO.of(10, 0),
                null,
                null);
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(criteria)
                .post(getBaseUrl() + "/search")
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    public void update() {
        var tenant = insertTenantEntity();
        var tenantDTO = TenantDTO
                .builder()
                .id(tenant.getId())
                .email(randomEmail())
                .hotline(randomPhoneNumber())
                .version(tenant.getVersion())
                .build();
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(tenantDTO)
                .put(getBaseUrl() + "/{id}", tenant.getId())
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    public void delete() {
        var tenant = insertTenantEntity();
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .delete(getBaseUrl() + "/{id}", tenant.getId())
                .then()
                .log().all()
                .statusCode(200);
    }
}
