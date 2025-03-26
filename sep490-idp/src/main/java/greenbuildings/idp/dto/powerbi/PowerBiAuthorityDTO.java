package greenbuildings.idp.dto.powerbi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record PowerBiAuthorityDTO(
        UUID id,
        int version,
        @NotBlank String note,
        @NotNull LocalDateTime expirationTime,
        LocalDateTime lastUsed) {
}
