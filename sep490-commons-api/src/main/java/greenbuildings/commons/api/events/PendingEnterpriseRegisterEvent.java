package greenbuildings.commons.api.events;

import greenbuildings.commons.api.security.UserRole;
import greenbuildings.commons.api.utils.CommonConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import static greenbuildings.commons.api.utils.CommonConstant.VIETNAME_TAX_CODE;
import static greenbuildings.commons.api.utils.CommonConstant.VIETNAM_ENTERPRISE_HOTLINE_PATTERN;

@Builder
public record PendingEnterpriseRegisterEvent(
        @Size(min = 1, max = 255, message = "{validation.enterpriseName.invalid}") String name,
        @NotNull @Email String enterpriseEmail,
        @NotBlank @Pattern(regexp = VIETNAME_TAX_CODE, message = "{validation.enterpriseTaxCode.invalid}") String taxCode,
        @NotBlank @Pattern(regexp = VIETNAM_ENTERPRISE_HOTLINE_PATTERN, message = "{validation.enterpriseHotline.invalid}") String hotline,
        @NotNull UserRole role,
        @NotBlank String address,
        @NotBlank String businessLicenseImageUrl,
        String representativeName,
        String representativePosition,
        String representativeContact){
    public static final String TOPIC = "enterprise-create-event";
}
