package greenbuildings.enterprise.dtos.emission_activities;

import java.util.List;
import java.util.UUID;

public record ActivityCriteria(
        String name,
        String category,
        List<UUID> buildings,
        List<UUID> factors
) {
}
