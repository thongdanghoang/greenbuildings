package greenbuildings.idp.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.commons.api.utils.CommonConstant;

import java.util.UUID;

public record EnterpriseUserDTO(
        UUID id,
        @NotBlank String name,
        @NotBlank @Pattern(regexp = CommonConstant.EMAIL_PATTERN) String email,
        @NotNull UserRole role) {
}
