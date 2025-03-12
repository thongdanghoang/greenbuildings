package greenbuildings.enterprise.dtos;

import java.util.List;
import java.util.UUID;

public record EmissionActivityDTO (
        UUID id,
        int version,
        UUID buildingID,
        List<EmissionActivityRecordDTO> records,
        UUID emissionFactorID,
        UUID emissionSourceID,
        String name,
        String type,
        String category,
        String description,
        int quantity
) {
}
