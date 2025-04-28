package greenbuildings.commons.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import greenbuildings.commons.api.enums.SortDirection;

/**
 * Represents a sorting specification with a field name and direction.
 *
 * @param field     The name of the field to sort by
 * @param direction The direction (ascending or descending) to sort in
 */
public record SortDTO(
        @NotBlank
        String field,
        @NotNull
        SortDirection direction
) {
    public static SortDTO of(String field, SortDirection direction) {
        return new SortDTO(field, direction);
    }
}
