package greenbuildings.enterprise.dtos;

import greenbuildings.commons.api.BaseDTO;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.With;

import java.util.Set;
import java.util.UUID;

@Builder
public record BuildingDTO(
        UUID id,
        int version,
        @NotBlank String name,
        @NotBlank String address,
        @Min(0) long numberOfDevices,
        @DecimalMin("-90.0") @DecimalMax("90.0") double latitude,
        @DecimalMin("-180.0") @DecimalMax("180.0") double longitude,
        @With SubscriptionDTO subscriptionDTO,
        Set<EmissionActivityDTO> emissionActivities
) implements BaseDTO {
}
