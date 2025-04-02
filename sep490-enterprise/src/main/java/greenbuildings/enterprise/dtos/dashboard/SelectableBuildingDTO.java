package greenbuildings.enterprise.dtos.dashboard;

import greenbuildings.commons.api.BaseDTO;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record SelectableBuildingDTO(
        UUID id,
        int version,
        @NotBlank String name) implements BaseDTO {
}
