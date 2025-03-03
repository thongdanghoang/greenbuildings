package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.SubscriptionDTO;
import greenbuildings.enterprise.entities.SubscriptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubscriptionMapper {

    SubscriptionDTO toDto(SubscriptionEntity subscriptionEntity);
}
