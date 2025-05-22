package alatoo.edu.edtechmentorshipplatform.enums;

import java.util.Arrays;

/**
 * Represents the status of a message in the mentorship platform.
 */
public enum MessageStatus {

    SENT("Message has been sent"),
    DELIVERED("Message has been delivered"),
    READ("Message has been read");

    private final String description;

    MessageStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static MessageStatus fromValue(String value) {
        return Arrays.stream(MessageStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown message status: " + value));
    }

    @Override
    public String toString() {
        return name() + " (" + description + ")";
    }
}
