package greenbuildings.commons.springfw.impl.exceptions.handlers;

import greenbuildings.commons.api.exceptions.BusinessErrorResponse;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.exceptions.TechnicalErrorResponse;
import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.commons.api.utils.MDCContext;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@Slf4j
public class RestExceptionHandler {
    
    private static TechnicalErrorResponse technicalError(Throwable ex, String errorMsg) {
        log.error("Technical exception occurred. Correlation ID: {}", MDC.get(MDCContext.CORRELATION_ID), ex);
        return new TechnicalErrorResponse(MDC.get(MDCContext.CORRELATION_ID), errorMsg);
    }
    
    private static BusinessErrorResponse businessError(BusinessException ex) {
        log.error("Business exception occurred. Correlation ID: {}", MDC.get(MDCContext.CORRELATION_ID), ex);
        return new BusinessErrorResponse(
                MDC.get(MDCContext.CORRELATION_ID),
                ex.getField(),
                ex.getI18nKey(),
                ex.getArgs()
        );
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<TechnicalErrorResponse> handleGenericException(Exception ex) {
        // log.error("Unhandled exception occurred. Correlation ID: {}", MDC.get(MDCContext.CORRELATION_ID), ex);
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
    public ResponseEntity<BusinessErrorResponse> handleAccessDeniedException(AuthorizationDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new BusinessErrorResponse(MDC.get(MDCContext.CORRELATION_ID), "validation.authorizeDenied"));
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
