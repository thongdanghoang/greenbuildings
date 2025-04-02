package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.dashboard.EnterpriseDashboardDTO;
import greenbuildings.enterprise.entities.EnterpriseDashboardEntity;
import greenbuildings.enterprise.entities.EnterpriseEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EnterpriseDashboardMapper {
    
    EnterpriseDashboardDTO toEnterpriseDashboardDTO(EnterpriseDashboardEntity source);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "enterprise", expression = "java(enterprise)")
    EnterpriseDashboardEntity createEnterpriseDashboardEntity(EnterpriseDashboardDTO source, @Context EnterpriseEntity enterprise);
    
    EnterpriseDashboardEntity updateEnterpriseDashboardEntity(EnterpriseDashboardDTO source, @MappingTarget EnterpriseDashboardEntity target);
    
}