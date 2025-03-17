package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreditPackageVersionDTO(UUID id, int version, @Positive int numberOfCredits, @Min(1) long price, boolean active) {
}
