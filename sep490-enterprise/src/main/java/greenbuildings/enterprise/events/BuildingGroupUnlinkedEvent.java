package greenbuildings.enterprise.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class BuildingGroupUnlinkedEvent extends ApplicationEvent {
    
    private final UUID buildingId;
    
    public BuildingGroupUnlinkedEvent(Object source, UUID buildingId) {
        super(source);
        this.buildingId = buildingId;
    }
}
