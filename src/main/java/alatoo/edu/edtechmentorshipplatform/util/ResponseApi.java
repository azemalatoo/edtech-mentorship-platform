package alatoo.edu.edtechmentorshipplatform.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class ResponseApi<T> {
    private T result;
    private ResponseCode code;
    private String exceptionMessage;

    public ResponseApi(T result, ResponseCode code) {
        this.result = result;
        this.code = code;
    }

    public ResponseApi(T result, ResponseCode code, String exceptionMessage) {
        this.result = result;
        this.code = code;
        this.exceptionMessage = exceptionMessage;
    }
}
