package greenbuildings.idp.dto;

import greenbuildings.idp.validators.ToBeValidated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static greenbuildings.commons.api.utils.CommonConstant.VIETNAME_TAX_CODE;
import static greenbuildings.commons.api.utils.CommonConstant.VIETNAM_ENTERPRISE_HOTLINE_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewEnterpriseDTO extends ToBeValidated {

    @Size(min = 1, max = 255, message = "{validation.enterpriseName.invalid}")
    private String name;
    
    @Email
    private String enterpriseEmail;

    @Pattern(regexp = VIETNAME_TAX_CODE, message = "{validation.enterpriseTaxCode.invalid}")
    private String taxCode;

    @Pattern(regexp = VIETNAM_ENTERPRISE_HOTLINE_PATTERN, message = "{validation.enterpriseHotline.invalid}")
    private String hotline;
}
