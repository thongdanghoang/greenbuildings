package greenbuildings.enterprise.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record InviteTenantToBuildingGroup(
        UUID buildingId,
        String tenantEmail,
        UUID selectedGroupId) {
}
