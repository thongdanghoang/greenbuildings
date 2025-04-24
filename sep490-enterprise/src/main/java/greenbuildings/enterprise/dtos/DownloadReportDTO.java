package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record DownloadReportDTO(@NotNull @NotEmpty List<UUID> selectedActivities) {
}