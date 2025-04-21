package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.GroupItemDTO;
import greenbuildings.enterprise.dtos.NewGroupItemDTO;
import greenbuildings.enterprise.entities.GroupItemEntity;
import greenbuildings.enterprise.mappers.decorators.GroupItemMapperDecorator;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
@DecoratedWith(GroupItemMapperDecorator.class)
public interface GroupItemMapper {
    GroupItemEntity toEntity(GroupItemDTO dto);
    
    GroupItemDTO toDTO(GroupItemEntity entity);
    
    @Mapping(target = "buildingGroup", ignore = true)
    @Mapping(target = "records", ignore = true)
    GroupItemEntity toEntity(NewGroupItemDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GroupItemEntity partialUpdate(GroupItemDTO dto, @MappingTarget GroupItemEntity entity);
} 