package greenbuildings.commons.api.dto;


public record SearchCriteriaDTO<T>(
        PageDTO page,
        SortDTO sort,
        T criteria
) {
    public static <T> SearchCriteriaDTO<T> of(PageDTO page, SortDTO sort, T criteria) {
        return new SearchCriteriaDTO<>(page, sort, criteria);
    }
}
