package greenbuildings.enterprise.dtos;

import java.util.UUID;

public record EmissionActivityCriteria(
        UUID buildingGroupId,
        UUID buildingId,
        String name) {
}
