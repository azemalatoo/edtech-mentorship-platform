package alatoo.edu.edtechmentorshipplatform.enums;

public enum MessageStatus {
    /** Message has been sent by the sender. */
    SENT,
    /** Message has been delivered to the recipient's client. */
    DELIVERED,
    /** Recipient has read the message. */
    READ,
    /** Sending or delivery failed. */
    FAILED
}
