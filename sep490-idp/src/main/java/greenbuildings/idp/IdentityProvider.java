package greenbuildings.idp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@ComponentScan(basePackages = {"commons.springfw", "greenbuildings.idp"})
@EnableJpaAuditing
@EnableMethodSecurity(jsr250Enabled = true)
public class IdentityProvider {
    
    public static void main(String[] args) {
        SpringApplication.run(IdentityProvider.class, args);
    }

//    @Bean
//    public FlywayMigrationStrategy cleanMigrateStrategy() {
//        return new FlywayMigrationStrategy() {
//            @Override
//            public void migrate(Flyway flyway) {
//                flyway.clean();
//                flyway.migrate();
//            }
//        };
//    }
    
}
