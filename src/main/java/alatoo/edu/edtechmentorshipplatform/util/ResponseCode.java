package alatoo.edu.edtechmentorshipplatform.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseCode {

    SUCCESS(0, HttpStatus.OK, "Operation completed successfully"),

    CREATED(1, HttpStatus.CREATED, "Resource created successfully"),

    BAD_REQUEST(2, HttpStatus.BAD_REQUEST, "The request was invalid or cannot be served"),

    NOT_FOUND(3, HttpStatus.NOT_FOUND, "The requested resource was not found"),

    INTERNAL_SERVER_ERROR(4, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected server error occurred"),

    VALIDATION_ERROR(5, HttpStatus.BAD_REQUEST, "Validation exception"),

    ACCESS_NOT_ALLOWED(6, HttpStatus.FORBIDDEN, "Access Denied"),

    UNAUTHORIZED(7, HttpStatus.UNAUTHORIZED, "Unauthorized");


    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    ResponseCode(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @JsonProperty("code")
    public int getCode() {
        return code;
    }

    @JsonProperty("httpStatus")
    public int getHttpStatus() {
        return httpStatus.value();
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
}
