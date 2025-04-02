package greenbuildings.idp.dto.powerbi;

import greenbuildings.commons.api.security.PowerBiScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record PowerBiAuthorityDTO(
        UUID id,
        int version,
        @NotBlank String note,
        @NotNull LocalDateTime expirationTime,
        @NotNull PowerBiScope scope,
        Set<UUID> buildings,
        LocalDateTime lastUsed) {
}
