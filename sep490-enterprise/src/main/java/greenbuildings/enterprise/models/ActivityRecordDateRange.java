package greenbuildings.enterprise.models;

import java.time.LocalDate;

/**
 * NOTE:
 * Models representing data structures used in the service layer.
 * Models mapping to View at controller layer.
 * Views should be used in controller layer for output only.
 * DTOs should be used in controller layer for input only.
 */
public record ActivityRecordDateRange(
        LocalDate startDate,
        LocalDate endDate
) {
}
