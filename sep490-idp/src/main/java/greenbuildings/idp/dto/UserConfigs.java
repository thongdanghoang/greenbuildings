package greenbuildings.idp.dto;

import lombok.Builder;

@Builder
public record UserConfigs(
        String language
) {
}
