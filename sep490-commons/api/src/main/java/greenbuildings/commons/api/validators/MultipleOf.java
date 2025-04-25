package greenbuildings.commons.api.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MultipleOfValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultipleOf {
    int value();
    
    String message() default "multipleOf"; // i18n key
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
