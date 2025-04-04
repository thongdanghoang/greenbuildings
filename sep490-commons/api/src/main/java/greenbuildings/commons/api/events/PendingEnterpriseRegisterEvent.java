package greenbuildings.commons.api.events;

import greenbuildings.commons.api.utils.CommonConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder(toBuilder = true)
public record PendingEnterpriseRegisterEvent(
        @NotBlank String name,
        @NotBlank @Pattern(regexp = CommonConstant.EMAIL_PATTERN) String email,
        @NotBlank @Pattern(regexp = CommonConstant.VIETNAM_ENTERPRISE_HOTLINE_PATTERN) String hotline,
        @NotBlank @Pattern(regexp = CommonConstant.VIETNAME_TAX_CODE) String taxCode
) {
    public static final String TOPIC = "enterprise-create-event";
}
