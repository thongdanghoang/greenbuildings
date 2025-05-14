package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

import static greenbuildings.commons.api.utils.CommonConstant.VIETNAME_TAX_CODE;

public record TenantDetailDTO(
        UUID id,
        int version,
        @NotNull @NotBlank String name,
        @NotNull @NotBlank String email,
        @NotNull @NotBlank String address,
        @NotNull @NotBlank String hotline,
        @NotNull @NotBlank @Pattern(regexp = VIETNAME_TAX_CODE, message = "format.enterpriseTaxCode") String taxCode,
        String businessLicenseImageUrl,
        String representativeName,
        String representativePosition,
        String representativeContact
) {
}
