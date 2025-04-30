package greenbuildings.enterprise.views.buildings.details;

import java.util.UUID;

public record TenantView(
        UUID id,
        String name,
        UUID buildingId,
        UUID buildingGroupId,
        String buildingGroupName
) {
}
