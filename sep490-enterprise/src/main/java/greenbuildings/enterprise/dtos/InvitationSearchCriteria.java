package greenbuildings.enterprise.dtos;

import greenbuildings.enterprise.enums.InvitationStatus;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record InvitationSearchCriteria(
        UUID enterpriseId,
        UUID buildingId,
        UUID buildingGroupId,
        String tenantEmail,
        InvitationStatus status
) {
}
