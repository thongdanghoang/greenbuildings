package greenbuildings.enterprise.dtos;

import greenbuildings.commons.api.BaseDTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Builder
public record BuildingDTO(
        UUID id,
        int version,
        @NotBlank String name,
        @NotBlank String address,
        @Min(1) int numberOfLevels,
        @DecimalMin("0.0") double area,
        @DecimalMin("-90.0") @DecimalMax("90.0") double latitude,
        @DecimalMin("-180.0") @DecimalMax("180.0") double longitude,
        @With SubscriptionDTO subscriptionDTO,
        Set<BuildingGroupDTO> buildingGroups,
        @PositiveOrZero BigDecimal limit
) implements BaseDTO {
}
