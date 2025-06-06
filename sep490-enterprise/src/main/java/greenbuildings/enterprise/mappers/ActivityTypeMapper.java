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
    @Mapping(target = "buildingId", source = "building.id")
    ActivityTypeDTO toDTO(ActivityTypeEntity entity);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "building.id", source = "buildingId")
    ActivityTypeEntity toEntity(ActivityTypeDTO dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "building.id", source = "buildingId")
    ActivityTypeEntity updateEntity(ActivityTypeDTO dto, @MappingTarget ActivityTypeEntity entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "building.id", source = "buildingId")
    ActivityTypeEntity createEntity(ActivityTypeDTO dto);
} 