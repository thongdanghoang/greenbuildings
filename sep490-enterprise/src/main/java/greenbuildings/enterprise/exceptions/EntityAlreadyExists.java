package greenbuildings.enterprise.exceptions;

import greenbuildings.commons.api.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class EntityAlreadyExists extends BusinessException {
    
    public EntityAlreadyExists(String field, String i18n) {
        super(field, i18n, Collections.emptyList(), HttpStatus.CONFLICT.value());
    }
}
