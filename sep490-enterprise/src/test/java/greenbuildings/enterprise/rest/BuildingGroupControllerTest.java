package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.dto.PageDTO;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.TestcontainersConfigs;
import greenbuildings.enterprise.dtos.BuildingGroupCriteria;
import greenbuildings.enterprise.dtos.CreateBuildingGroupDTO;
import greenbuildings.enterprise.dtos.InviteTenantToBuildingGroup;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BuildingGroupControllerTest extends TestcontainersConfigs {
    
    @Override
    protected String getBaseUrl() {
        return "/building-groups";
    }
    
    @Test
    void createBuildingGroup() {
        var buildingGroupDTO = CreateBuildingGroupDTO
                .builder()
                .name(randomString())
                .description(randomString())
                .buildingId(insertBuildingEntity().getId())
                .build();
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .body(buildingGroupDTO)
                .when()
                .post(getBaseUrl())
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    void update() {
        var buildingGroupEntity = insertBuildingGroupEntity(insertBuildingEntity());
        var buildingGroupDTO = CreateBuildingGroupDTO
                .builder()
                .name(randomString())
                .description(randomString())
                .buildingId(insertBuildingEntity().getId())
                .build();
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(buildingGroupDTO)
                .put(getBaseUrl() + "/{id}", buildingGroupEntity.getId())
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    void findById() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "/{id}", insertBuildingGroupEntity(insertBuildingEntity()).getId())
                .then()
                .log().all()
                .statusCode(200);
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
    void findByBuildingId() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "/building/{buildingId}", insertBuildingGroupEntity(insertBuildingEntity()).getId())
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    void search() {
        var buildingEntity = insertBuildingEntity();
        insertBuildingGroupEntity(buildingEntity);
        insertBuildingGroupEntity(buildingEntity);
        insertBuildingGroupEntity(buildingEntity);
        insertBuildingGroupEntity(buildingEntity);
        insertBuildingGroupEntity(buildingEntity);
        var criteria = SearchCriteriaDTO.of(
                PageDTO.of(10, 0),
                null,
                BuildingGroupCriteria
                        .builder()
                        .buildingId(buildingEntity.getId())
                        .build()
                                           );
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
    void delete() {
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .delete(getBaseUrl() + "/{id}", insertBuildingGroupEntity(insertBuildingEntity()).getId())
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    void getAvailableBuildingGroups() {
        var buildingEntity = insertBuildingEntity();
        insertBuildingGroupEntity(buildingEntity);
        insertBuildingGroupEntity(buildingEntity);
        insertBuildingGroupEntity(buildingEntity);
        insertBuildingGroupEntity(buildingEntity);
        insertBuildingGroupEntity(buildingEntity);
        
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "/building/{buildingId}/available", buildingEntity.getId())
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    void inviteTenant() {
        var buildingEntity = insertBuildingEntity();
        
        var payload = InviteTenantToBuildingGroup
                .builder()
                .buildingId(buildingEntity.getId())
                .tenantEmail(randomEmail())
                .buildingGroupIds(List.of(
                        insertBuildingGroupEntity(buildingEntity).getId(),
                        insertBuildingGroupEntity(buildingEntity).getId(),
                        insertBuildingGroupEntity(buildingEntity).getId()
                                         ))
                .build();
        
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(payload)
                .post(getBaseUrl() + "/invite")
                .then()
                .log().all()
                .statusCode(200);
    }
    
    @Test
    void findByTenantId() {
        var tenantEntity = insertTenantEntity();
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "/tenant")
                .then()
                .log().all()
                .statusCode(200);
    }
    
}
