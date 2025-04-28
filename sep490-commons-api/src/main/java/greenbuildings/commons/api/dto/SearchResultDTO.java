package greenbuildings.commons.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SearchResultDTO<T>(
        @NotNull List<T> results,
        @Min(0) long totalElements
) {
    public static <T> SearchResultDTO<T> of(@NotNull List<T> results, long totalElements) {
        return new SearchResultDTO<>(results, totalElements);
    }
}
