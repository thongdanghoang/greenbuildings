package greenbuildings.enterprise.utils;

import greenbuildings.enterprise.entities.enums.UnitType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculationUtilsTest {
    
    @Test
    void testCalculateCO2e() {
        double b = CalculationUtils.calculateCO2e(100.123,965.654,0.2189);
        assertEquals(27196.4435, b, 0.001);
    }
    
    @ParameterizedTest
    @CsvSource({
            "KILOGRAM, MEGAGRAM, 1000, 1",
            "LITER, CUBIC_METER, 1000, 1",
            "GRAM, KILOGRAM, 1000, 1"
    })
    void testValidConversions(UnitType from, UnitType to, double value, double expected) {
        BigDecimal result = CalculationUtils.convertUnit(from, to, BigDecimal.valueOf(value));
        assertEquals(0, result.compareTo(BigDecimal.valueOf(expected)));
    }
    
    @Test
    void testTrailingZeros() {
        BigDecimal bigDecimal = CalculationUtils
                .convertUnit(UnitType.MEGAGRAM, UnitType.MEGAGRAM, BigDecimal.valueOf(1.000000000));
        assertEquals(BigDecimal.valueOf(1), bigDecimal);
    }
    
    @Test
    void testHalfUpRounded() {
        BigDecimal bigDecimal = CalculationUtils
                .convertUnit(UnitType.MEGAGRAM, UnitType.MEGAGRAM, BigDecimal.valueOf(1.0));
        assertEquals(BigDecimal.valueOf(1), bigDecimal);
    }
    
    @Test
    void testInvalidConversion() {
        assertThrows(IllegalArgumentException.class, () -> {
            CalculationUtils.convertUnit(UnitType.KILOGRAM, UnitType.LITER, BigDecimal.ONE);
        });
    }
    
}
