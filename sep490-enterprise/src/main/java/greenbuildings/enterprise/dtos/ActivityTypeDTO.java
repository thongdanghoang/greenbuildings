package greenbuildings.enterprise.dtos;

import java.util.UUID;

public record ActivityTypeDTO(
        UUID id,
        int version,
        String name,
        String description,
        UUID enterpriseId) {
}

