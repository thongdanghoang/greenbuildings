package greenbuildings.enterprise.enums;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum EmissionUnit {
    // Mass (Base: MEGAGRAM = 1 tonne)
    GRAM(UnitCategory.MASS, new BigDecimal("1000000")),      // 1,000,000 g = 1 Mg (tấn)
    KILOGRAM(UnitCategory.MASS, new BigDecimal("1000")),     // 1,000 kg = 1 Mg
    MEGAGRAM(UnitCategory.MASS, new BigDecimal("1")),        // 1 Mg = 1 Mg (tấn)
    GIGAGRAM(UnitCategory.MASS, new BigDecimal("0.001")),    // 0.001 Gg = 1 Mg (1 Gg = 1,000 Mg)
    TERAGRAM(UnitCategory.MASS, new BigDecimal("0.000001")), // 0.000001 Tg = 1 Mg (1 Tg = 1,000,000 Mg)
    
    // Volume (Base: CUBIC_METER)
    MILLILITER(UnitCategory.VOLUME, new BigDecimal("1000000")),     // 1,000,000 mL = 1 m³
    CENTILITER(UnitCategory.VOLUME, new BigDecimal("100000")),      // 100,000 cL = 1 m³
    DECILITER(UnitCategory.VOLUME, new BigDecimal("10000")),        // 10,000 dL = 1 m³
    LITER(UnitCategory.VOLUME, new BigDecimal("1000")),            // 1,000 L = 1 m³
    CUBIC_METER(UnitCategory.VOLUME, new BigDecimal("1")),         // 1 m³ = 1 m³ (mét khối)
    UK_GALLON(UnitCategory.VOLUME, new BigDecimal("219.969")),     // 219.969 UK gal = 1 m³
    US_GALLON(UnitCategory.VOLUME, new BigDecimal("264.172")),     // 264.172 US gal = 1 m³
    BARREL(UnitCategory.VOLUME, new BigDecimal("6.289")),          // 6.289 barrels = 1 m³
    PECK(UnitCategory.VOLUME, new BigDecimal("113.51")),           // 113.51 pecks = 1 m³
    THOUSAND_CUBIC_METER(UnitCategory.VOLUME, new BigDecimal("0.001")), // 0.001 x 1000 m³ = 1 m³
    MILLION_CUBIC_METER(UnitCategory.VOLUME, new BigDecimal("0.000001")), // 0.000001 x 1,000,000 m³ = 1 m³
    
    // Electric (Base: KWH)
    WH(UnitCategory.ELECTRIC, new BigDecimal("1000")),    // 1,000 Wh = 1 kWh
    KWH(UnitCategory.ELECTRIC, new BigDecimal("1")),      // 1 kWh = 1 kWh (Kilowatt-giờ)
    
    // Energy (Base: TERAJOULE)
    TERAJOULE(UnitCategory.ENERGY, new BigDecimal("1")),     // 1 TJ = 1 TJ
    GIGAJOULE(UnitCategory.ENERGY, new BigDecimal("1000")),  // 1,000 GJ = 1 TJ
    MEGAJOULE(UnitCategory.ENERGY, new BigDecimal("1000000")), // 1,000,000 MJ = 1 TJ
    
    PERCENT(UnitCategory.PERCENT, new BigDecimal("1")),
    PIECES(UnitCategory.PIECES, new BigDecimal("1"));
    
    private final UnitCategory category;
    private final BigDecimal baseConvertRatio;
    
    EmissionUnit(UnitCategory category, BigDecimal baseConvertRatio) {
        this.category = category;
        this.baseConvertRatio = baseConvertRatio;
    }
    
    public boolean isSameCategory(EmissionUnit other) {
        return this.category == other.category;
    }
}
