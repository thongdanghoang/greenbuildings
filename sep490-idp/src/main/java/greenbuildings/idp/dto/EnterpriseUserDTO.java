package greenbuildings.idp.dto;


import greenbuildings.commons.api.security.UserRole;
import greenbuildings.commons.api.utils.CommonConstant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record EnterpriseUserDTO(
        UUID id,
        @NotBlank String name,
        @NotBlank @Pattern(regexp = CommonConstant.EMAIL_PATTERN) String email,
        @NotNull UserRole role,
        boolean emailVerified) {
}
