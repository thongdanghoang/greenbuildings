package greenbuildings.commons.api.dto;

import jakarta.validation.constraints.NotNull;
import greenbuildings.commons.api.enums.DateBoundary;

import java.time.LocalDate;

public record DateRangeDTO(
        @NotNull
        LocalDate from,
        @NotNull
        LocalDate to,
        @NotNull
        DateBoundary fromBoundary,
        @NotNull
        DateBoundary toBoundary
) {

    public static DateRangeDTO of(LocalDate from, LocalDate to, DateBoundary fromBoundary, DateBoundary toBoundary) {
        return new DateRangeDTO(from, to, fromBoundary, toBoundary);
    }

    public DateRangeDTO {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }
    }

    public boolean isDateInRange(LocalDate date) {
        boolean startCondition =
                fromBoundary == DateBoundary.INCLUSIVE
                        ? !date.isBefore(from)
                        : date.isAfter(from);
        boolean endCondition =
                toBoundary == DateBoundary.INCLUSIVE
                        ? !date.isAfter(to)
                        : date.isBefore(to);
        return startCondition && endCondition;
    }
}
