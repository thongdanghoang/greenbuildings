package greenbuildings.enterprise.mappers;

import greenbuildings.commons.api.events.PendingEnterpriseRegisterEvent;
import greenbuildings.enterprise.dtos.TenantDTO;
import greenbuildings.enterprise.dtos.TenantDetailDTO;
import greenbuildings.enterprise.entities.TenantEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TenantMapper {
    TenantEntity createTenant(TenantDTO dto);
    
    @Mapping(target = "buildingGroups", ignore = true)
    TenantDTO toDTO(TenantEntity entity);
    
    @Mapping(target = "buildingGroups", ignore = true)
    @Mapping(target = "activityTypes", ignore = true)
    TenantDTO toBasicDTO(TenantEntity entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    TenantEntity createTenant(PendingEnterpriseRegisterEvent enterpriseCreateEvent);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TenantEntity partialUpdate(TenantDTO dto, @MappingTarget TenantEntity entity);

    TenantDetailDTO toDetailDTO(TenantEntity entity);
}
