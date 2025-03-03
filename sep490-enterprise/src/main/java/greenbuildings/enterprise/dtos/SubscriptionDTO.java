package greenbuildings.enterprise.dtos;

import java.time.LocalDate;
import java.util.UUID;

public record SubscriptionDTO(UUID id, int version, LocalDate startDate,  LocalDate endDate, int maxNumberOfDevices) {
}
