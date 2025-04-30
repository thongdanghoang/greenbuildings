package greenbuildings.commons.api.dto;


import jakarta.validation.Valid;

public record SearchCriteriaDTO<T>(
        @Valid PageDTO page,
        @Valid SortDTO sort,
        @Valid T criteria
) {
    public static <T> SearchCriteriaDTO<T> of(PageDTO page, SortDTO sort, T criteria) {
        return new SearchCriteriaDTO<>(page, sort, criteria);
    }
}
