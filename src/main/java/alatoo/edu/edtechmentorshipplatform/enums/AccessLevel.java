package alatoo.edu.edtechmentorshipplatform.enums;

import java.util.Arrays;

/**
 * Represents different levels of content visibility and access within the platform.
 */
public enum AccessLevel {

    PRIVATE("Visible only to the owner"),
    MENTEE("Accessible to mentees"),
    MENTOR("Accessible to mentors"),
    PUBLIC("Visible to everyone");

    private final String description;

    AccessLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static AccessLevel fromValue(String value) {
        return Arrays.stream(AccessLevel.values())
                .filter(level -> level.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown access level: " + value));
    }

    @Override
    public String toString() {
        return name() + " (" + description + ")";
    }
}