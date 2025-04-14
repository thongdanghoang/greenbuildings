package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.BuildingGroupDTO;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BuildingGroupMapper {
    BuildingGroupEntity toEntity(BuildingGroupDTO dto);
    
    BuildingGroupDTO toDTO(BuildingGroupEntity entity);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BuildingGroupEntity partialUpdate(BuildingGroupDTO dto, @MappingTarget BuildingGroupEntity entity);
} 