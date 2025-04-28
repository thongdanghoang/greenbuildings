package greenbuildings.commons.api.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.FieldNameConstants;
import greenbuildings.commons.api.security.BuildingPermissionRole;

import java.util.UUID;

@FieldNameConstants
public record BuildingPermissionDTO(
        UUID buildingId,
        @NotNull BuildingPermissionRole role
) {
}
