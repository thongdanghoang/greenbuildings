package greenbuildings.idp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ValidateOTPRequest(@NotNull @NotBlank @Length(min = 6, max = 6) String otpCode) {
}
