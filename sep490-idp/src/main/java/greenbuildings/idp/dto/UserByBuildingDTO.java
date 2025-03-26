package greenbuildings.idp.dto;

import greenbuildings.commons.api.security.BuildingPermissionRole;
import jakarta.validation.constraints.NotBlank;
import lombok.With;


import java.util.UUID;

public record UserByBuildingDTO(UUID id, @NotBlank String name, @With BuildingPermissionRole role) {
}
