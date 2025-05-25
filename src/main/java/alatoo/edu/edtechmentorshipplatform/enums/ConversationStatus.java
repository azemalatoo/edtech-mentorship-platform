package alatoo.edu.edtechmentorshipplatform.enums;

/**
 * Status of a conversation thread.
 */
public enum ConversationStatus {
    /** New conversation, open for messaging. */
    OPEN,
    /** Conversation has been closed by a participant. No new messages allowed. */
    CLOSED,
    /** Conversation has been archived (e.g., after inactivity). */
    ARCHIVED
}
