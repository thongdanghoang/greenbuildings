package greenbuildings.enterprise.entities.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum UnitType {
    // Mass based
    GRAM(1_000_000),        // Gam
    KILOGRAM(1000),         // Kilôgam
    MEGAGRAM(1),            // Megagam (Tấn)
    GIGAGRAM(0.001),        // Gigagam (Nghìn tấn)
    TERAGRAM(0.000_001),    // Teragram (Nghìn tấn)
    
    // Volume based
    MILLILITER(1_000_000),  // Mililit (ml)
    CENTILITER(100_000),    // Centilit (cl)
    DECILITER(10_000),      // Decilit (dl)
    LITER(1_000),           // Lít (l)
    CUBIC_METER(1),         // Mét khối (m³)
    UK_GALLON(219.969),     // Gallon Anh (UK Gallon)
    US_GALLON(264.172),     // Gallon Mỹ (US Gallon)
    BARREL(6.289),          // Barrel (Thùng)
    PECK(113.51),            // Peck (US Dry Measure)
    THOUSAND_CUBIC_METER(0.001), // 1000 m³
    MILLION_CUBIC_METER(0.000_001), // 1000000 m³
    
    // Electric based
    Wh(1000),       // watt-hour
    KWh(1),         // Kilowatt-giờ
    
    // Percentage
    PERCENT(1),     // Phần trăm
    
    // Energy based
    TERAJOULE(1);    // Terajoule
    
    private double baseConvertRatio;
    
    private UnitType(double baseConvertRatio) {
        this.baseConvertRatio = baseConvertRatio;
    }
    
    public static List<UnitType> getMassBasedUnits() {
        return List.of(KILOGRAM, GRAM, GIGAGRAM, MEGAGRAM, TERAGRAM);
    }
    
    public static List<UnitType> getVolumeBasedUnits() {
        return List.of(CUBIC_METER, LITER, DECILITER, CENTILITER, MILLILITER, UK_GALLON, US_GALLON, BARREL, PECK);
    }
    
    public static List<UnitType> getElectricBasedUnits() {
        return List.of(Wh, KWh);
    }
    
    public static List<UnitType> getPercentageBasedUnits() {
        return List.of(PERCENT);
    }
    
    public static List<UnitType> getEnergyBasedUnits() {
        return List.of(TERAJOULE);
    }
    
    public static boolean isSameCategory(UnitType a, UnitType b) {
        return (getMassBasedUnits().contains(a) && getMassBasedUnits().contains(b))
               || (getVolumeBasedUnits().contains(a) && getVolumeBasedUnits().contains(b))
               || (getPercentageBasedUnits().contains(a) && getPercentageBasedUnits().contains(b))
               || (getEnergyBasedUnits().contains(a) && getEnergyBasedUnits().contains(b))
               || (getElectricBasedUnits().contains(a) && getElectricBasedUnits().contains(b));
    }
}
