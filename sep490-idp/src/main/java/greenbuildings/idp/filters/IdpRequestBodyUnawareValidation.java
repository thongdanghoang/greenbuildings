package greenbuildings.idp.filters;

import commons.springfw.impl.filters.RequestBodyUnawareValidation;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "greenbuildings.idp.rest")
public class IdpRequestBodyUnawareValidation extends RequestBodyUnawareValidation {

}
