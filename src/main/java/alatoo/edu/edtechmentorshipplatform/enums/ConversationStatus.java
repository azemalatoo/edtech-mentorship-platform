package alatoo.edu.edtechmentorshipplatform.enums;

import java.util.Arrays;

/**
 * Represents the status of a conversation between users on the mentorship platform.
 */
public enum ConversationStatus {

    ACTIVE("Conversation is ongoing"),
    ARCHIVED("Conversation is archived but not deleted"),
    CLOSED("Conversation is closed and cannot be continued");

    private final String description;

    ConversationStatus(String description) {
        this.description = description;
    }


    public String getDescription() {
        return description;
    }


    public static ConversationStatus fromValue(String value) {
        return Arrays.stream(ConversationStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown conversation status: " + value));
    }

    @Override
    public String toString() {
        return name() + " (" + description + ")";
    }
}
