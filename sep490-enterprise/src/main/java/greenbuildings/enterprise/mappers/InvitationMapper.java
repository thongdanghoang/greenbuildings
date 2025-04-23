package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.InvitationDTO;
import greenbuildings.enterprise.entities.InvitationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvitationMapper {
    
    @Mapping(target = "buildingGroup.emissionActivities", ignore = true)
    @Mapping(target = "buildingGroup.tenant", ignore = true)
    @Mapping(target = "buildingGroup.building.buildingGroups", ignore = true)
    @Mapping(target = "buildingGroup.building.subscriptionDTO", ignore = true)
    InvitationDTO toDTO(InvitationEntity entity);
    
    InvitationEntity toEntity(InvitationDTO dto);
}
