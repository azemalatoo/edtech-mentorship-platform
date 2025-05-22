package alatoo.edu.edtechmentorshipplatform.enums;

import java.util.Arrays;

/**
 * Represents the supported languages
 */
public enum Lang {

    KY("Kyrgyz"),
    RU("Russian"),
    EN("English");

    private final String description;

    Lang(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Lang fromValue(String value) {
        return Arrays.stream(Lang.values())
                .filter(lang -> lang.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown language: " + value));
    }

    @Override
    public String toString() {
        return name() + " (" + description + ")";
    }
}
