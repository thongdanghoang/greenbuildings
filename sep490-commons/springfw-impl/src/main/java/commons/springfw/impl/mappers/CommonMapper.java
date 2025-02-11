package commons.springfw.impl.mappers;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import green_buildings.commons.api.dto.PageDTO;
import green_buildings.commons.api.dto.SearchResultDTO;
import green_buildings.commons.api.dto.SortDTO;
import green_buildings.commons.api.enums.SortDirection;
import green_buildings.commons.api.utils.CommonConstant;

import java.util.Objects;
import java.util.function.Function;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public class CommonMapper {
    
    public static Pageable toPageable(PageDTO searchPageDto, SortDTO sortDto) {
        var pageNumber = 0;
        var pageSize = CommonConstant.DEFAULT_PAGE_SIZE;
        if (Objects.nonNull(searchPageDto)) {
            pageNumber = Math.clamp(searchPageDto.pageNumber(), 0, Integer.MAX_VALUE);
            pageSize = Math.clamp(searchPageDto.pageSize(), 1, CommonConstant.MAX_PAGE_SIZE);
        }
        if (Objects.nonNull(sortDto)
            && StringUtils.isNotBlank(sortDto.field())) {
            var direction = Objects.equals(sortDto.direction(), SortDirection.ASC)
                            ? Sort.Direction.ASC
                            : Sort.Direction.DESC;
            return PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortDto.field()));
        }
        return PageRequest.of(pageNumber, pageSize);
    }
    
    public static <T, R> SearchResultDTO<R> toSearchResultDTO(Page<T> page, Function<T, R> mapper) {
        return SearchResultDTO.of(
                page.getContent().stream().map(mapper).toList(),
                page.getTotalElements()
                                 );
    }
}
