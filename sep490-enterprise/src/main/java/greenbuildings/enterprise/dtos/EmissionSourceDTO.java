package greenbuildings.enterprise.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record EmissionSourceDTO(
        UUID id,
        String nameVN,
        String nameEN,
        String nameZH,
        int version
) {}
