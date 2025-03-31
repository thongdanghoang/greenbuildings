package greenbuildings.enterprise.dtos.dashboard;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record EnterpriseDashboardDTO(
        UUID id,
        int version,
        @NotBlank String title,
        @NotBlank String source) {
}
