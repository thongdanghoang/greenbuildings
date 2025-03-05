package greenbuildings.enterprise.utils;


import greenbuildings.enterprise.entities.enums.UnitType;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class for greenhouse gas (GHG) emission calculations.
 * <p>
 * This class provides methods to calculate carbon dioxide equivalent (CO₂e) emissions
 * based on Global Warming Potential (GWP) values from the IPCC Fifth Assessment Report (AR5).
 * </p>
 *
 * <h3>Reference:</h3>
 * <ul>
 *   <li>IPCC AR5 (2015). "Global Warming Potential Values".</li>
 *   <li>Source: <a href="https://ghgprotocol.org/sites/default/files/ghgp/Global-Warming-Potential-Values%20%28Feb%2016%202016%29_1.pdf">
 *       GHG Protocol - Global Warming Potential Values</a></li>
 * </ul>
 *
 * @author Tran Gia Bao - GreenBuildings
 * @version 1.0 - 03 March 2025
 */
public class CalculationUtils {
    
    // References: AR5 2015
    // https://ghgprotocol.org/sites/default/files/ghgp/Global-Warming-Potential-Values%20%28Feb%2016%202016%29_1.pdf
    public static final double GWP_CO2 = 1;
    public static final double GWP_CH4 = 28;
    public static final double GWP_N2O = 265;
    
    
    private CalculationUtils() {
        // Private constructor for utility class
    }
    
    public static double calculateCO2e(double co2, double ch4, double n2o) {
        return (GWP_CO2 * co2) + (GWP_CH4 * ch4) + (GWP_N2O * n2o);
    }
    
    public static BigDecimal convertUnit(UnitType from, UnitType to, BigDecimal value) {
        if (!UnitType.isSameCategory(from, to)) {
            throw new IllegalArgumentException("Cannot convert " + from + " to " + to);
        }
        BigDecimal fromFactor = BigDecimal.valueOf(from.getBaseConvertRatio());
        BigDecimal toFactor = BigDecimal.valueOf(to.getBaseConvertRatio());
        
        return value.multiply(toFactor)
                    .divide(fromFactor, 20, RoundingMode.HALF_UP)
                    .stripTrailingZeros();
    }
    
}
