package greenbuildings.enterprise.dtos;

import greenbuildings.commons.api.BaseDTO;
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
        @With SubscriptionDTO subscriptionDTO,
        Set<EmissionActivityDTO> emissionActivities
) implements BaseDTO {
}
