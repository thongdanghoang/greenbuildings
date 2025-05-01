package greenbuildings.enterprise.dtos;

import greenbuildings.enterprise.enums.InvitationStatus;

import java.util.UUID;

public record InvitationResponseDTO(
        UUID id,
        InvitationStatus status
) {
}
