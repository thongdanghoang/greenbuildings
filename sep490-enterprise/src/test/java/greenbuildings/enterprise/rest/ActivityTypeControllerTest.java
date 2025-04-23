package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.dto.PageDTO;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.TestcontainersConfigs;
import greenbuildings.enterprise.dtos.ActivityTypeCriteriaDTO;
import greenbuildings.enterprise.dtos.ActivityTypeDTO;
import greenbuildings.enterprise.dtos.EnterpriseDTO;
import greenbuildings.enterprise.dtos.TenantDTO;
import greenbuildings.enterprise.mappers.EnterpriseMapper;
import greenbuildings.enterprise.mappers.TenantMapper;
import greenbuildings.enterprise.services.EnterpriseService;
import greenbuildings.enterprise.services.TenantService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class ActivityTypeControllerTest extends TestcontainersConfigs {
    
    @Autowired
    private EnterpriseService enterpriseService;
    
    @Autowired
    private TenantService tenantService;
    
    @Autowired
    private TenantMapper tenantMapper;
    
    @Autowired
    private EnterpriseMapper enterpriseMapper;
    
    @Override
    protected String getBaseUrl() {
        return "/activity-types";
    }
    
    private UUID createEnterprise() {
        var enterprise = EnterpriseDTO
                .builder()
                .email(randomEmail())
                .name(randomString())
                .hotline(randomPhoneNumber())
                .build();
        return enterpriseService.createEnterprise(
                enterpriseMapper.createEnterprise(enterprise)
                                                 );
    }
    
    private UUID createTenant() {
        var tenant = TenantDTO
                .builder()
                .email(randomEmail())
                .name(randomString())
                .hotline(randomPhoneNumber())
                .build();
        return tenantService.create(tenantMapper.createTenant(tenant)).getId();
    }
    
    @Test
    void createOrUpdate_returns201() {
        var payload = ActivityTypeDTO
                .builder()
                .name("Etiam vestibulum nulla sed magna.")
                .description("Donec iaculis viverra ante et.")
                .tenantID(createTenant())
                .build();
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(payload)
                .post(getBaseUrl() + "/create")
                .then()
                .statusCode(201);
    }
    
    @Test
    void create_returns200() {
        var payload = ActivityTypeDTO
                .builder()
                .name("Etiam vestibulum nulla sed magna.")
                .description("Donec iaculis viverra ante et.")
                .tenantID(createTenant())
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
    void create_invalidEnterpriseId_returns404() {
        var payload = ActivityTypeDTO
                .builder()
                .name("Nam eu faucibus nunc. Proin.")
                .description("Maecenas nisi sem, consequat et.")
                .tenantID(UUID.randomUUID())
                .build();
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(payload)
                .post(getBaseUrl())
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
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(payload)
                .post(getBaseUrl())
                .then()
                .statusCode(404);
    }
    
    @Test
    void searchActivityType_returns200() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .body(new SearchCriteriaDTO<ActivityTypeCriteriaDTO>(PageDTO.of(10, 0), null, new ActivityTypeCriteriaDTO("Lorem Ipsum", UUID.randomUUID())))
                .when()
                .post(getBaseUrl() + "/search")
                .then()
                .statusCode(200);
    }
    
    @Test
    void findAll_returns200() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl())
                .then()
                .statusCode(200);
    }
    
    @Test
    void findByEnterpriseId_returns404() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .param("tenantId", UUID.randomUUID())
                .get(getBaseUrl())
                .then()
                .statusCode(404);
    }
    
    @Test
    void findByEnterpriseId_returns200() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .param("tenantId", createTenant())
                .get(getBaseUrl())
                .then()
                .statusCode(200);
    }
    
    @Test
    void findById_returns404() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "/" + UUID.randomUUID())
                .then()
                .statusCode(404);
    }
    
    @Test
    void delete_returns417() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(Collections.emptySet())
                .delete(getBaseUrl())
                .then()
                .log().all()
                .statusCode(417);
    }
    
    @Test
    void delete_returnsNoContent() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(Set.of(UUID.randomUUID()))
                .delete(getBaseUrl())
                .then()
                .log().all()
                .statusCode(204);
    }
    
}
