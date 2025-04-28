package greenbuildings.commons.api.events;

import greenbuildings.commons.api.security.PowerBiScope;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record PowerBiAccessTokenAuthResult(
        UUID enterpriseId,
        PowerBiScope scope,
        Set<UUID> buildings) {
}
