package greenbuildings.enterprise;

import com.fasterxml.jackson.annotation.JsonProperty;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import greenbuildings.enterprise.entities.TenantEntity;
import greenbuildings.enterprise.repositories.BuildingGroupRepository;
import greenbuildings.enterprise.repositories.BuildingRepository;
import greenbuildings.enterprise.repositories.EnterpriseRepository;
import greenbuildings.enterprise.repositories.TenantRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;
import org.testcontainers.utility.DockerImageName;

import java.security.SecureRandom;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class TestcontainersConfigs {
    
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.4-alpine");
    static final GenericContainer<?> idP = new GenericContainer<>("thongdh3401/keycloak:24.0.5");
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.6.1")
    );
    
    static {
        kafka.start();
        postgres.start();
        idP.withExposedPorts(8180)
           .withCommand("start-dev --http-port 8180")
           .waitingFor(Wait.forHttp("/realms/greenbuildings/.well-known/openid-configuration"))
           .start();
    }
    
    @LocalServerPort
    Integer port;
    
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port + "/enterprise";
    }

//    @BeforeAll
//    static void beforeAll() {
//        postgres.start();
//        idP.withExposedPorts(8180)
//                .withCommand("start-dev --http-port 8180")
//                .waitingFor(Wait.forHttp("/realms/greenbuildings/.well-known/openid-configuration"))
//                .start();
//    }

//    @AfterAll
//    static void afterAll() {
//        postgres.stop();
//        idP.stop();
//    }
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("logging.level.org.flywaydb", () -> "DEBUG");
        Supplier<Object> callable = () -> StringSubstitutor.replace(
                "http://localhost:${mappedPort}/realms/greenbuildings", Map
                        .of("mappedPort", getMappedPort(idP, 8180)));
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", callable);
    }
    
    protected static String getMappedPort(GenericContainer<?> container, int originalPort) {
        return container.getMappedPort(originalPort).toString();
    }
    
    protected UUID enterpriseOwnerEnterpriseId() {
        return UUID.fromString("664748fa-1312-4456-a88c-1ef187ec9510");
    }
    
    protected RequestSpecification asEnterpriseOwner() {
        return RestAssured.given().auth().oauth2(getToken("enterprise.owner@greenbuildings.com", "enterprise.owner"));
    }
    
    protected RequestSpecification asSystemAdmin() {
        return RestAssured.given().auth().oauth2(getToken("system.admin@greenbuildings.com", "system.admin"));
    }
    
    private String getToken(String username, String password) {
        var tokenUrl = StringSubstitutor.replace(
                "http://localhost:${mappedPort}/realms/greenbuildings/protocol/openid-connect/token", Map
                        .of("mappedPort", getMappedPort(idP, 8180)));
        var response = RestAssured
                .given()
                .contentType(ContentType.URLENC)
                .formParam("client_id", "testcontainer")
                .formParam("grant_type", "password")
                .formParam("username", username)
                .formParam("password", password)
                .post(tokenUrl)
                .then()
                .statusCode(200)
                .extract()
                .as(Token.class);
        
        return response.accessToken();
    }
    
    record Token(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("expires_in") long expiresIn,
            @JsonProperty("refresh_expires_in") long refreshExpiresIn,
            @JsonProperty("refresh_token") String refreshToken,
            @JsonProperty("token_type") String tokenType,
            @JsonProperty("not-before-policy") int notBeforePolicy,
            @JsonProperty("session_state") String sessionState,
            @JsonProperty("scope") String scope
    ) {
    }
    
    protected String randomEmail() {
        return randomString() + "@greenbuildings.com";
    }
    
    protected String randomPhoneNumber() {
        return "0" + RandomStringUtils.randomNumeric(9);
    }
    
    protected String randomString() {
        var chars = "abcdefghjklmnpqrstuvwxyz".toCharArray();
        return RandomStringUtils.random(8, 0, chars.length, false, false, chars, new SecureRandom());
    }
    
    abstract protected String getBaseUrl();
    
    @Autowired
    protected BuildingRepository buildingRepository;
    
    @Autowired
    protected EnterpriseRepository enterpriseRepository;
    
    @Autowired
    private BuildingGroupRepository buildingGroupRepository;
    
    @Autowired
    private TenantRepository tenantRepository;
    
    protected BuildingGroupEntity insertBuildingGroupEntity(BuildingEntity building) {
        var buildingGroupEntity = new BuildingGroupEntity(building);
        return buildingGroupRepository.save(buildingGroupEntity);
    }
    
    protected BuildingEntity insertBuildingEntity() {
        var buildingEntity = new BuildingEntity();
        buildingEntity.setName(randomString());
        buildingEntity.setAddress(randomString());
        buildingEntity.setAddress(randomString());
        buildingEntity.setAddress(randomString());
        buildingEntity.setEnterprise(enterpriseRepository.findById(enterpriseOwnerEnterpriseId()).orElseThrow());
        return buildingRepository.save(buildingEntity);
    }
    
    protected TenantEntity insertTenantEntity() {
        var tenantEntity = new TenantEntity(randomEmail(), randomPhoneNumber());
        return tenantRepository.save(tenantEntity);
    }
}
