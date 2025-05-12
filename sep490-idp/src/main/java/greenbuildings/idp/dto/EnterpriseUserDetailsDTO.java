package greenbuildings.idp.dto;

import greenbuildings.commons.api.BaseDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.commons.api.utils.CommonConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.UUID;

public record EnterpriseUserDetailsDTO(
        UUID id,
        int version,
        LocalDateTime createdDate,
        @NotBlank @Pattern(regexp = CommonConstant.EMAIL_PATTERN) String email,
        @Pattern(regexp = CommonConstant.VIETNAM_PHONE_PATTERN) String phone,
        boolean emailVerified,
        @NotBlank String firstName,
        @NotBlank String lastName,
        UserRole role
) implements BaseDTO {
}
