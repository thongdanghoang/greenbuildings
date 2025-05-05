package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record DownloadReportDTO(
        @NotNull UUID buildingID,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull @NotEmpty List<UUID> selectedGroups
) {
}