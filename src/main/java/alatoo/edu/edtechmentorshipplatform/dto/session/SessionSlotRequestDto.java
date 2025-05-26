package alatoo.edu.edtechmentorshipplatform.dto.session;

import alatoo.edu.edtechmentorshipplatform.enums.SessionProviderType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SessionSlotRequestDto {
    @NotNull(message = "availableFrom is required")
    private LocalDateTime availableFrom;

    @NotNull(message = "availableTo is required")
    private LocalDateTime availableTo;

    @NotNull(message = "providerType is required")
    private SessionProviderType providerType;

    @NotNull(message = "meetingLink is required")
    @Size(max = 1000, message = "meetingLink too long")
    private String meetingLink;

    private String notes;
}