package greenbuildings.idp.dto;

import greenbuildings.commons.api.security.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static greenbuildings.commons.api.utils.CommonConstant.VIETNAME_TAX_CODE;
import static greenbuildings.commons.api.utils.CommonConstant.VIETNAM_ENTERPRISE_HOTLINE_PATTERN;

public record RegisterEnterpriseDTO(
        @Size(min = 1, max = 255, message = "{validation.enterpriseName.invalid}") String name,
        @Email String enterpriseEmail,
        @NotBlank @Pattern(regexp = VIETNAME_TAX_CODE, message = "{validation.enterpriseTaxCode.invalid}") String taxCode,
        @NotBlank @Pattern(regexp = VIETNAM_ENTERPRISE_HOTLINE_PATTERN, message = "{validation.enterpriseHotline.invalid}") String hotline,
        @NotNull UserRole role,
        @NotBlank String address,
        @NotBlank String businessLicenseImageUrl,
        String representativeName,
        String representativePosition,
        String representativeContact
) {
    public RegisterEnterpriseDTO withEmail(String email) {
        return new RegisterEnterpriseDTO(
                this.name(),
                email,
                this.taxCode(),
                this.hotline(),
                this.role(),
                this.address(),
                this.businessLicenseImageUrl(),
                this.representativeName(),
                this.representativePosition(),
                this.representativeContact()
        );
    }
}
