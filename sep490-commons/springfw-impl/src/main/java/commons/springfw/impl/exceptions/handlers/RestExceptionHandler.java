package commons.springfw.impl.exceptions.handlers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sep490.common.api.exceptions.BusinessErrorResponse;
import sep490.common.api.exceptions.BusinessException;
import sep490.common.api.exceptions.TechnicalErrorResponse;
import sep490.common.api.exceptions.TechnicalException;
import sep490.common.api.utils.MDCContext;

import java.util.NoSuchElementException;

@Slf4j
public class RestExceptionHandler {
    
    private static TechnicalErrorResponse technicalError(Throwable exception, String errorMsg) {
        return new TechnicalErrorResponse(MDC.get(MDCContext.CORRELATION_ID), errorMsg);
    }
    
    private static BusinessErrorResponse businessError(BusinessException exception) {
        return new BusinessErrorResponse(
                MDC.get(MDCContext.CORRELATION_ID),
                exception.getField(),
                exception.getI18nKey(),
                exception.getArgs()
        );
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<TechnicalErrorResponse> handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred. Correlation ID: {}", MDC.get(MDCContext.CORRELATION_ID), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(technicalError(ex, ex.getMessage()));
    }
    
    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<TechnicalErrorResponse> handleTechnicalException(TechnicalException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(technicalError(ex, ex.getMessage()));
    }
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BusinessErrorResponse> handleBusinessException(BusinessException ex) {
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(businessError(ex));
    }
    
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<TechnicalErrorResponse> handleAccessDeniedException(AuthorizationDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(technicalError(ex, ex.getMessage()));
    }
    
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<TechnicalErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(technicalError(ex, ex.getMessage()));
    }
    
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<TechnicalErrorResponse> handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(technicalError(ex, ex.getMessage()));
    }
}
