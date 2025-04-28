package commons.springfw.impl.exceptions;

import org.springframework.http.HttpStatus;
import greenbuildings.commons.api.exceptions.BusinessErrorParam;
import greenbuildings.commons.api.exceptions.BusinessException;

import java.util.List;

public class JakartaValidationException extends BusinessException {
    public JakartaValidationException(String field, String i18nKey, List<BusinessErrorParam> args) {
        super(field, i18nKey, args, HttpStatus.BAD_REQUEST.value());
    }
}
