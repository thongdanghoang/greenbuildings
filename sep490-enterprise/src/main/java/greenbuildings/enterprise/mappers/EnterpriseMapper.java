package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.EnterpriseDTO;
import greenbuildings.enterprise.dtos.EnterpriseDetailDTO;
import greenbuildings.enterprise.entities.EnterpriseEntity;
import greenbuildings.enterprise.mappers.decorators.EnterpriseMapperDecorator;
import greenbuildings.commons.api.events.PendingEnterpriseRegisterEvent;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@DecoratedWith(EnterpriseMapperDecorator.class)
public interface EnterpriseMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "wallet", ignore = true)
    EnterpriseEntity createEnterprise(EnterpriseDTO enterpriseDTO);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "wallet", ignore = true)
    EnterpriseEntity createEnterprise(PendingEnterpriseRegisterEvent event);

    // Map from entity to detail DTO
    EnterpriseDetailDTO toEnterpriseDetailDTO(EnterpriseEntity entity);

    // Update entity from detail DTO
    void updateEntityFromDetailDTO(EnterpriseDetailDTO dto, @MappingTarget EnterpriseEntity entity);
}
