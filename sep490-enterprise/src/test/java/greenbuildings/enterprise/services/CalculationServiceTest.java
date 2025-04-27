package greenbuildings.enterprise.services;

import greenbuildings.enterprise.TestcontainersConfigs;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalculationServiceTest extends TestcontainersConfigs {
    
    @Override
    protected String getBaseUrl() {
        return StringUtils.EMPTY;
    }
    
    @Autowired
    private CalculationService calculationService;
    
    @Test
    void calculateDirectly() {
        var emissionActivityEntity = insertActivity(true);
        var records = new ArrayList<EmissionActivityRecordEntity>();
        var startDate = LocalDate.now().minusMonths(60);
        for (int i = 0; i < 10; i++) {
            var endDate = startDate.plusMonths(i);
            records.add(insertRecord(emissionActivityEntity, startDate, endDate));
            startDate = endDate.plusDays(1);
        }
        var results = calculationService
                .calculate(emissionActivityEntity.getId(), records);
        for (var result : results) {
            Assertions.assertTrue(result.getGhg().compareTo(BigDecimal.ZERO) > 0);
        }
    }
    
    @Test
    void calculateIndirectly() {
        var emissionActivityEntity = insertActivity(false);
        var records = new ArrayList<EmissionActivityRecordEntity>();
        var startDate = LocalDate.now().minusMonths(60);
        for (int i = 0; i < 10; i++) {
            var endDate = startDate.plusMonths(i);
            records.add(insertRecord(emissionActivityEntity, startDate, endDate));
            startDate = endDate.plusDays(1);
        }
        var results = calculationService
                .calculate(emissionActivityEntity.getId(), records);
        for (var result : results) {
            Assertions.assertTrue(result.getGhg().compareTo(BigDecimal.ZERO) > 0);
        }
    }
}
