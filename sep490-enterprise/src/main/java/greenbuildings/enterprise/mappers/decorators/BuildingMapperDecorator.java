package greenbuildings.enterprise.mappers.decorators;

import greenbuildings.enterprise.dtos.BuildingDTO;
import greenbuildings.enterprise.dtos.SubscriptionDTO;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.mappers.BuildingMapper;

import greenbuildings.enterprise.mappers.SubscriptionMapper;
import greenbuildings.enterprise.repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public abstract class BuildingMapperDecorator implements BuildingMapper {
    @Autowired
    @Qualifier("delegate")
    private BuildingMapper delegate;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Override
    public BuildingDTO toDto(BuildingEntity buildingEntity) {
        BuildingDTO buildingDTO = delegate.toDto(buildingEntity);
        var subscriptions = subscriptionRepository.findAllValidSubscriptions(LocalDate.now(), buildingEntity.getId());
        if (subscriptions.isEmpty()) {
            return buildingDTO;
        }
        var subscription = subscriptions.getFirst();
        SubscriptionDTO subscriptionDTO = subscriptionMapper.toDto(subscription);
        return buildingDTO.withSubscriptionDTO(subscriptionDTO);
    }
}
