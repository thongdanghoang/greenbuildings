package greenbuildings.enterprise.dtos;

import greenbuildings.enterprise.enums.InvitationStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record InvitationSearchCriteria(@NotNull UUID enterpriseId, UUID buildingId, UUID buildingGroupId, String tenantEmail, InvitationStatus status) {
}
