package greenbuildings.commons.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Represents pagination parameters for API requests.
 *
 * @param pageSize   The number of items per page (1-100)
 * @param pageNumber The zero-based page number
 */
public record PageDTO(
        @Min(1)
        @Max(100)
        int pageSize,
        @Min(0)
        int pageNumber
) {
    public static PageDTO of(int pageSize, int pageNumber) {
        return new PageDTO(pageSize, pageNumber);
    }
}
