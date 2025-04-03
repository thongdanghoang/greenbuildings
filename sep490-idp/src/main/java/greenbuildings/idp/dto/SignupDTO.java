package greenbuildings.idp.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import greenbuildings.idp.validators.ToBeValidated;

import static greenbuildings.commons.api.utils.CommonConstant.*;

@Getter
@Setter
public class SignupDTO extends ToBeValidated {

    @Pattern(regexp = EMAIL_PATTERN, message = "{validation.email.invalid}")
    @Size(min = 1, max = 255, message = "{validation.email.length}")
    private String email;

    @Pattern(regexp = PASSWORD_PATTERN, message = "{validation.password.invalid}")
    private String password;

    @Pattern(regexp = PASSWORD_PATTERN, message = "{validation.confirmPassword.invalid}")
    private String confirmPassword;
}