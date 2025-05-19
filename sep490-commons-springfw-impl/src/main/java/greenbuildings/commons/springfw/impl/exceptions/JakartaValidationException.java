package greenbuildings.commons.springfw.impl.exceptions;

import greenbuildings.commons.api.exceptions.BusinessErrorParam;
import greenbuildings.commons.api.exceptions.BusinessException;

import org.springframework.http.HttpStatus;

import java.util.List;

public class JakartaValidationException extends BusinessException {
    public JakartaValidationException(String field, String i18nKey, List<BusinessErrorParam> args) {
        super(field, i18nKey, args, HttpStatus.BAD_REQUEST.value());
    }
}
