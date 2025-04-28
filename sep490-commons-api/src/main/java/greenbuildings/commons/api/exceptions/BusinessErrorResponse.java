package greenbuildings.commons.api.exceptions;


import greenbuildings.commons.api.enums.ServerErrorType;

import java.util.List;

/**
 * The correlation ID is a unique identifier for the error. The field is the name of the field
 * associated with the error. The i18nKey is a key used for internationalization. The args is a list
 * of parameters for the error.
 */
public record BusinessErrorResponse(
        String correlationId,
        String field,
        String i18nKey,
        List<BusinessErrorParam> args
) implements ServerErrorResponse {
    @Override
    public ServerErrorType getErrorType() {
        return ServerErrorType.BUSINESS;
    }
    
    public BusinessErrorResponse(BusinessException exception, String correlationId) {
        this(correlationId, exception.field, exception.i18nKey, exception.args);
    }
    
    public BusinessErrorResponse(String correlationId, String i18nKey) {
        this(correlationId, null, i18nKey, null);
    }
}
