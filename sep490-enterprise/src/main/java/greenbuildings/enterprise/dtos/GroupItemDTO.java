package greenbuildings.enterprise.dtos;

import greenbuildings.commons.api.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record GroupItemDTO(
        UUID id,
        int version,
        @NotBlank String itemName,
        String description,
        Set<EmissionActivityRecordDTO> records
) implements BaseDTO {
} 