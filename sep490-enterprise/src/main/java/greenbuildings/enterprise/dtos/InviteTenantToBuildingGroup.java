package greenbuildings.enterprise.dtos;

import java.util.List;
import java.util.UUID;

public record InviteTenantToBuildingGroup(UUID buildingId, String tenantEmail, List<UUID> buildingGroupIds) {
}
