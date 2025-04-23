package greenbuildings.enterprise.dtos;

import greenbuildings.enterprise.enums.InvitationStatus;

import java.util.UUID;

public record InvitationDTO(
        UUID id,
        String email,
        int version,
        InvitationStatus status,
        BuildingGroupDTO buildingGroup
) {
}
