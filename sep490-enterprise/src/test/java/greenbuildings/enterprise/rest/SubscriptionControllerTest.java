package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.TestcontainersConfigs;
import greenbuildings.enterprise.dtos.SubscribeRequestDTO;
import greenbuildings.enterprise.enums.TransactionType;
import greenbuildings.enterprise.repositories.WalletRepository;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

public class SubscriptionControllerTest extends TestcontainersConfigs {
    
    @Autowired
    private WalletRepository walletRepository;
    
    @Override
    protected String getBaseUrl() {
        return "/subscription";
    }
    
    @Test
    void subscribe_notEnoughCredit() {
        // given
        var buildingEntity = insertBuildingEntity();
        var request = SubscribeRequestDTO
                .builder()
                .buildingId(buildingEntity.getId())
                .months(6)
                .numberOfDevices(10000)
                .type(TransactionType.NEW_PURCHASE) // TODO: we can detect from BE instead?
                .build();
        
        // when
        // then
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post(getBaseUrl())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_EXPECTATION_FAILED);
    }
    
    @Test
    void subscribe() {
        var walletEntity = walletRepository.findByEnterpriseId(enterpriseOwnerEnterpriseId());
        walletEntity.setBalance(Double.parseDouble(RandomStringUtils.randomNumeric(9, 12)));
        walletRepository.save(walletEntity);
        // given
        var buildingEntity = insertBuildingEntity();
        var request = SubscribeRequestDTO
                .builder()
                .buildingId(buildingEntity.getId())
                .months(6)
                .numberOfDevices(10000)
                .type(TransactionType.NEW_PURCHASE) // TODO: we can detect from BE instead?
                .build();
        
        // when
        // then
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post(getBaseUrl())
                .then()
                .log().all()
                .statusCode(201);
    }
    
    @Test
    void upgrade() {
        var walletEntity = walletRepository.findByEnterpriseId(enterpriseOwnerEnterpriseId());
        walletEntity.setBalance(Double.parseDouble(RandomStringUtils.randomNumeric(9, 12)));
        walletRepository.save(walletEntity);
        // given
        var buildingEntity = insertBuildingEntity();
        var request = SubscribeRequestDTO
                .builder()
                .buildingId(buildingEntity.getId())
                .months(Integer.parseInt(RandomStringUtils.randomNumeric(1, 2)))
                .numberOfDevices(Integer.parseInt(RandomStringUtils.randomNumeric(2, 3)))
                .type(TransactionType.NEW_PURCHASE) // TODO: we can detect from BE instead?
                .build();
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post(getBaseUrl())
                .then()
                .log().all()
                .statusCode(201);
        request = SubscribeRequestDTO
                .builder()
                .buildingId(buildingEntity.getId())
                .months(Integer.parseInt(RandomStringUtils.randomNumeric(1, 2)))
                .numberOfDevices(Integer.parseInt(RandomStringUtils.randomNumeric(2, 3)))
                .type(TransactionType.UPGRADE) // TODO: we can detect from BE instead?
                .build();
        
        // when
        // then
        asEnterpriseOwner()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post(getBaseUrl())
                .then()
                .log().all()
                .statusCode(201);
    }
    
}
