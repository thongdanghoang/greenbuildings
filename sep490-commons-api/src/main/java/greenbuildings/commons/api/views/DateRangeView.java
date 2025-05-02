package greenbuildings.commons.api.views;

import java.time.LocalDate;

public record DateRangeView(
        LocalDate fromInclusive,
        LocalDate toInclusive
) {
}
