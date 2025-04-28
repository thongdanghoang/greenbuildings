package greenbuildings.commons.api.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * An exception class that represent the business error for the application.
 */
@Getter
public class BusinessException extends RuntimeException {
    protected final String field;
    protected final String i18nKey;
    protected final ArrayList<BusinessErrorParam> args;
    protected final int httpStatus;
    
    private static final int EXPECTATION_FAILED = 417;
    
    /**
     * Constructs a new BusinessException with the specified field, i18nKey, args, and HTTP status.
     *
     * @param field   The name of the field associated with the error.
     * @param i18nKey The key used for internationalization.
     * @param args    The list of parameters for the error.
     */
    public BusinessException(String field, String i18nKey, List<BusinessErrorParam> args) {
        this(field, i18nKey, args, EXPECTATION_FAILED);
    }
    
    protected BusinessException(String field, String i18nKey, List<BusinessErrorParam> args, int httpStatus) {
        this.field = field;
        this.i18nKey = i18nKey;
        this.args = new ArrayList<>(args);
        this.httpStatus = httpStatus;
    }
    
    protected BusinessException(String field, String i18nKey, List<BusinessErrorParam> args, int httpStatus, Throwable cause) {
        super(cause);
        this.field = field;
        this.i18nKey = i18nKey;
        this.args = new ArrayList<>(args);
        this.httpStatus = httpStatus;
    }
    
    protected BusinessException(String field, String i18nKey, Map<String, Object> args) {
        this(field, i18nKey, args.entrySet().stream().map(entry -> new BusinessErrorParam(entry.getKey(), entry.getValue())).toList());
    }
    
    protected BusinessException(String i18nKey, List<BusinessErrorParam> args) {
        this(null, i18nKey, args);
    }
    
    public BusinessException(String i18nKey) {
        this(null, i18nKey, List.of());
    }
    
    public BusinessException(String field, String i18nKey) {
        this(field, i18nKey, List.of());
    }
    
    protected BusinessException(String i18nKey, int httpStatus, Throwable cause) {
        this(null, i18nKey, Collections.emptyList(), httpStatus, cause);
    }
}
