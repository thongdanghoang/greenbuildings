package greenbuildings.enterprise.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GwpConfiguration {
    AR4(1, 25, 298),
    AR5(1, 28, 265),
    AR6(1, 27, 273);
    private final double CO2;
    private final double CH4;
    private final double N2O;
}
