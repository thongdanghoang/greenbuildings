package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EmissionActivityRecordCriteria(
        @NotNull
        UUID emissionActivityId
) {
}