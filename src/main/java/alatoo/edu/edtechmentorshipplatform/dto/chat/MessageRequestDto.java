package alatoo.edu.edtechmentorshipplatform.dto.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageRequestDto {
    
    @NotBlank(message = "Message content cannot be blank")
    private String content;
    
    private boolean isImportant = false;
    
    private String messageType;
}
