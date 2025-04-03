package greenbuildings.enterprise.dtos;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DownloadReportDTO(@NotNull UUID buildingID, @NotNull LocalDate startDate, @NotNull LocalDate endDate, @NotNull @NotEmpty List<UUID> selectedActivities) {
}