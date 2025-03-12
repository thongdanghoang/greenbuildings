package greenbuildings.enterprise.dtos;

import java.util.UUID;

public record EmissionSourceDTO(
        UUID id,
        String nameVN,
        String nameEN,
        String nameZH,
        int version
) {}
