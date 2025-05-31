package greenbuildings.enterprise.dtos.assets;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssetDTO(
        UUID id,
        int version,
        @NotNull @NotBlank String name,
        @NotNull @NotBlank String code,
        String description,
        UUID buildingId
) {
}
