package greenbuildings.enterprise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ComponentScan(basePackages = {"greenbuildings.enterprise", "greenbuildings.commons"})
@EnableJpaAuditing
public class EnterpriseApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EnterpriseApplication.class, args);
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
