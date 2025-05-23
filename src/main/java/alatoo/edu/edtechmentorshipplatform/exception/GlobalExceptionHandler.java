package alatoo.edu.edtechmentorshipplatform.exception;

import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
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
    public ResponseApi<Void> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.debug("Validation failed: {}", errors);
        return new ResponseApi<>(null, ResponseCode.BAD_REQUEST, errors);
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
        log.error("Internal exception : ", ex);
        String detail = ex.getMessage() != null
                ? ex.getMessage()
                : ResponseCode.INTERNAL_SERVER_ERROR.getMessage();
        return new ResponseApi<>(null, ResponseCode.INTERNAL_SERVER_ERROR, detail);
    }
}
