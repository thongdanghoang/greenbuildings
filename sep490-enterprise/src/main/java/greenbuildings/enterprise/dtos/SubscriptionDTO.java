package greenbuildings.enterprise.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.UUID;

public record SubscriptionDTO(UUID id, int version, @JsonFormat(pattern = "dd/MM/yyyy")LocalDate startDate,  @JsonFormat(pattern = "dd/MM/yyyy")LocalDate endDate, int maxNumberOfDevices) {
}
