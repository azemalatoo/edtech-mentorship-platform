package alatoo.edu.edtechmentorshipplatform.util;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class ResponseApi<T> {
    private T result;
    private ResponseCode code;
    private String message;
}
