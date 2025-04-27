package greenbuildings.enterprise.dtos;

import greenbuildings.commons.api.BaseDTO;
import greenbuildings.commons.api.utils.CommonConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.UUID;

public record EnterpriseDetailDTO(
        UUID id,
        int version,
        @NotBlank String name,
        @NotBlank @Pattern(regexp = CommonConstant.EMAIL_PATTERN) String email,
        @NotBlank @Pattern(regexp = CommonConstant.VIETNAM_PHONE_PATTERN) String hotline
){
}
