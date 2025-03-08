package greenbuildings.enterprise.utils;


import greenbuildings.enterprise.enums.EmissionUnit;
import greenbuildings.enterprise.enums.GwpConfiguration;

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
    static GwpConfiguration defaultGwpConfig = GwpConfiguration.AR5;
    
    
    private CalculationUtils() {
        // Private constructor for utility class
    }
    
    public static double calculateCO2e(double co2, double ch4, double n2o) {
        return defaultGwpConfig.getCO2() * co2
               + defaultGwpConfig.getCH4() * ch4
               + defaultGwpConfig.getN2O() * n2o;
    }
    
    public static BigDecimal convertUnit(EmissionUnit from, EmissionUnit to, BigDecimal value) {
        if (!from.isSameCategory(to)) {
            throw new IllegalArgumentException("Cannot convert " + from + " to " + to);
        }
        BigDecimal fromFactor = from.getBaseConvertRatio();
        BigDecimal toFactor = to.getBaseConvertRatio();
        
        return value.multiply(toFactor)
                    .divide(fromFactor, 20, RoundingMode.HALF_UP)
                    .stripTrailingZeros();
    }
    
}
