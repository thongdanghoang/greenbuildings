package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.TenantDTO;
import greenbuildings.enterprise.entities.TenantEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TenantMapper {
    TenantEntity toEntity(TenantDTO dto);
    
    @Mapping(target = "buildingGroups", ignore = true)
    TenantDTO toDTO(TenantEntity entity);
    
    @Mapping(target = "buildingGroups", ignore = true)
    @Mapping(target = "activityTypes", ignore = true)
    TenantDTO toBasicDTO(TenantEntity entity);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TenantEntity partialUpdate(TenantDTO dto, @MappingTarget TenantEntity entity);
} 