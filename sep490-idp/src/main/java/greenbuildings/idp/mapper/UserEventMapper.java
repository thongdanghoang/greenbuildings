package greenbuildings.idp.mapper;

import greenbuildings.commons.api.events.PendingEnterpriseRegisterEvent;
import greenbuildings.idp.dto.RegisterEnterpriseDTO;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserEventMapper {
    
    @Valid PendingEnterpriseRegisterEvent toPendingEnterpriseRegisterEvent(RegisterEnterpriseDTO source);
    
}
