package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.GroupItemDTO;
import greenbuildings.enterprise.entities.GroupItemEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface GroupItemMapper {
    GroupItemEntity toEntity(GroupItemDTO dto);
    
    GroupItemDTO toDTO(GroupItemEntity entity);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GroupItemEntity partialUpdate(GroupItemDTO dto, @MappingTarget GroupItemEntity entity);
} 