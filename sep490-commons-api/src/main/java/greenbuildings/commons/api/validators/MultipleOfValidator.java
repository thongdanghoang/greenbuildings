package greenbuildings.commons.api.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MultipleOfValidator implements ConstraintValidator<MultipleOf, Integer> {
    
    private int multipleOf;
    
    @Override
    public void initialize(MultipleOf constraintAnnotation) {
        this.multipleOf = constraintAnnotation.value(); // Get the value from the annotation
    }
    
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value % multipleOf == 0;
    }
}
