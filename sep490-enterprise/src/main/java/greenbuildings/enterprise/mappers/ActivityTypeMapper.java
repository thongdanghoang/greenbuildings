package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.ActivityTypeDTO;
import greenbuildings.enterprise.entities.ActivityTypeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ActivityTypeMapper {
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "enterpriseId", source = "enterprise.id")
    ActivityTypeDTO toDTO(ActivityTypeEntity entity);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "enterprise.id", source = "enterpriseId")
    ActivityTypeEntity toEntity(ActivityTypeDTO dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "enterprise.id", source = "enterpriseId")
    void updateEntity(ActivityTypeDTO dto, @MappingTarget ActivityTypeEntity entity);
} 