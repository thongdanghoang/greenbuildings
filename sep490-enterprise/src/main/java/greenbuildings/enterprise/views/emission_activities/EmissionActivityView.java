package greenbuildings.enterprise.views.emission_activities;

import java.util.UUID;

public record EmissionActivityView(
        UUID id,
        int version,
        UUID building,
        UUID emissionFactor,
        String type,
        String name,
        String category,
        String description
) {
}
