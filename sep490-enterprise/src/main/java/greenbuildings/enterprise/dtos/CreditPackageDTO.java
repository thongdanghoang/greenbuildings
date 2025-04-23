package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record CreditPackageDTO(
        UUID id,
        int version,
        @Positive int numberOfCredits,
        @Min(1) long price,
        int discount) {
}
