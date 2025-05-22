package alatoo.edu.edtechmentorshipplatform.enums;

import java.util.Arrays;

/**
 * Represents the status of a learning goal in the mentorship platform.
 */
public enum GoalStatus {

    PENDING("Goal not started"),
    IN_PROGRESS("Goal in progress"),
    ACHIEVED("Goal completed");

    private final String description;

    GoalStatus(String description) {
        this.description = description;
    }


    public String getDescription() {
        return description;
    }

    /**
     * Converts a string to a GoalStatus enum, ignoring case.
     *
     * @param value the string value to convert
     * @return the matching GoalStatus
     * @throws IllegalArgumentException if no match is found
     */
    public static GoalStatus fromValue(String value) {
        return Arrays.stream(GoalStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown goal status: " + value));
    }

    @Override
    public String toString() {
        return name() + " (" + description + ")";
    }
}
