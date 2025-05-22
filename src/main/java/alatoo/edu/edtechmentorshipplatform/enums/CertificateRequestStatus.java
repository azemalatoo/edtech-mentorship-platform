package alatoo.edu.edtechmentorshipplatform.enums;

import java.util.Arrays;

/**
 * Represents the status of a certificate request in the mentorship platform.
 */
public enum CertificateRequestStatus {

    PENDING("Awaiting approval"),
    APPROVED("Request has been approved"),
    REJECTED("Request has been rejected");

    private final String description;

    CertificateRequestStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static CertificateRequestStatus fromValue(String value) {
        return Arrays.stream(CertificateRequestStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown certificate request status: " + value));
    }

    @Override
    public String toString() {
        return name() + " (" + description + ")";
    }
}
