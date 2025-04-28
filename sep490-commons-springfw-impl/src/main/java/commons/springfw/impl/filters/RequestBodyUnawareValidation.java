package commons.springfw.impl.filters;

import commons.springfw.impl.exceptions.JakartaValidationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Objects;

public class RequestBodyUnawareValidation extends RequestBodyAdviceAdapter {
    
    private final Validator validator;
    
    public RequestBodyUnawareValidation() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }
    
    @Override
    public Object afterBodyRead(Object body,
                                HttpInputMessage inputMessage,
                                MethodParameter parameter,
                                Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        
        var violations = Objects.requireNonNull(validator).validate(body);
        if (!CollectionUtils.isEmpty(violations)) {
            var violation = violations.stream().findFirst().get();
            throw new JakartaValidationException(
                    violation.getPropertyPath().toString(),
                    violation.getMessage(),
                    Collections.emptyList()
            );
        }
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
    
    @Override
    public boolean supports(MethodParameter methodParameter,
                            Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasParameterAnnotation(RequestBody.class);
    }
}
