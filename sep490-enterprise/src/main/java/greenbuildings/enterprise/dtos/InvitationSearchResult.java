package greenbuildings.enterprise.dtos;

import greenbuildings.enterprise.enums.InvitationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record InvitationSearchResult(UUID id, LocalDateTime createdDate, InvitationStatus status, String tenantEmail) {
}
