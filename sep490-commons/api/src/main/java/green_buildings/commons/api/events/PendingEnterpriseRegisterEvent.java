package green_buildings.commons.api.events;

import green_buildings.commons.api.utils.CommonConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder(toBuilder = true)
public record PendingEnterpriseRegisterEvent(
        @NotBlank String name,
        @NotBlank @Pattern(regexp = CommonConstant.EMAIL_PATTERN) String email,
        @NotBlank @Pattern(regexp = CommonConstant.VIETNAM_ENTERPRISE_HOTLINE_PATTERN) String hotline
) {
    public static final String TOPIC = "enterprise-create-event";
}
