package alatoo.edu.edtechmentorshipplatform.exception;

import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseApi<Void> handleEmailAlreadyExistException(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.debug("Validation failed: {}", errors);
        return new ResponseApi<>(null, ResponseCode.BAD_REQUEST, errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseApi<Void> handleEmailAlreadyExistException(EmailAlreadyExistsException ex) {
        log.debug("EmailAlreadyExistsException: ", ex);
        return new ResponseApi<>(null, ResponseCode.VALIDATION_ERROR, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseApi<Void> handleConstraintViolations(ConstraintViolationException ex) {
        String errors = ex.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.joining("; "));
        log.debug("Constraint violations: {}", errors);
        return new ResponseApi<>(null, ResponseCode.BAD_REQUEST, errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseApi<Void> handleUnreadableMessage(HttpMessageNotReadableException ex) {
        String detail = "Malformed request body: " + ex.getMostSpecificCause().getMessage();
        log.debug("Malformed JSON request", ex);
        return new ResponseApi<>(null, ResponseCode.BAD_REQUEST, detail);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseApi<Void> handleNotFound(NotFoundException ex) {
        log.debug("NotFoundException : ", ex);
        return new ResponseApi<>(null, ResponseCode.NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseApi<Void> handleAll(Exception ex) {
        log.error("Exception : ", ex);
        String detail = ex.getMessage() != null
                ? ex.getMessage()
                : ResponseCode.INTERNAL_SERVER_ERROR.getMessage();
        return new ResponseApi<>(null, ResponseCode.INTERNAL_SERVER_ERROR, detail);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EntityInUseException.class)
    public ResponseApi<Void> handleEntityInUse(EntityInUseException ex) {
        log.debug("EntityInUseException : ", ex);
        return new ResponseApi<>(null, ResponseCode.VALIDATION_ERROR, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseApi<Void> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
        log.debug("AccessDeniedException: {}", ex.getMessage());
        return new ResponseApi<>(null,
                ResponseCode.ACCESS_NOT_ALLOWED,
                "You do not have permission to perform this action");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseApi<Void> handleExpiredJwt(ExpiredJwtException ex) {
        log.debug("ExpiredJwtException: {}", ex.getMessage());
        return new ResponseApi<>(
                null,
                ResponseCode.UNAUTHORIZED,
                "Your session has expired. Please log in again."
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(RefreshTokenException.class)
    public ResponseApi<Void> handleRefreshTokenException(RefreshTokenException ex) {
        log.debug("RefreshTokenException: {}", ex.getMessage());
        return new ResponseApi<>(
                null,
                ResponseCode.UNAUTHORIZED,
                ex.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ResponseApi<Void> handleValidationException(ValidationException ex) {
        log.debug("ValidationException: {}", ex.getMessage());
        return new ResponseApi<>(
                null,
                ResponseCode.VALIDATION_ERROR,
                ex.getMessage()
        );
    }
}
