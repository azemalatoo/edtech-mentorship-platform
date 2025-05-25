package alatoo.edu.edtechmentorshipplatform.dto.session;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SessionSlotRequestDto {
    @NotNull(message = "availableFrom is required")
    private LocalDateTime availableFrom;
    @NotNull(message = "availableTo is required")
    private LocalDateTime availableTo;
}