package greenbuildings.enterprise.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GwpConfiguration {
    AR4(1, 25, 298), // IPCC AR4 (2007)
    AR5(1, 28, 265), // IPCC AR5 (2015) - Default use
    AR6(1, 27, 273); // IPCC AR6 (2021)
    private final double CO2;
    private final double CH4;
    private final double N2O;
}
