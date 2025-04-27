package greenbuildings.enterprise.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ActivityTypeDTO(
        UUID id,
        int version,
        String name,
        UUID buildingId,
        String description) {
}

