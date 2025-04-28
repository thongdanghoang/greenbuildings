package greenbuildings.commons.api.exceptions;

/**
 * An exception class that represents a runtime error for the application.
 */
public class TechnicalException extends RuntimeException {
    public TechnicalException(String message, Throwable e) {
        super(message, e);
    }

    public TechnicalException(String message) {
        super(message);
    }

    public TechnicalException(Throwable e) {
        super(e);
    }
}
