package greenbuildings.commons.api.events;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CommitEnterpriseRegisterEvent(
        @NotNull UUID enterpriseId
) {
    public static final String TOPIC = "enterprise-created-success-event";
}
