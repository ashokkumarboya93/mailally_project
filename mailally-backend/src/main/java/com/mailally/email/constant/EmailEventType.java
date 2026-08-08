package com.mailally.email.constant;

/**
 * Type-safe Enum representing standardized email lifecycle and engagement events.
 */
public enum EmailEventType {
    SENT,
    DELIVERED,
    OPENED,
    CLICKED,
    BOUNCED,
    COMPLAINT,
    UNSUBSCRIBED;

    public static EmailEventType fromString(String text) {
        if (text == null) return SENT;
        for (EmailEventType type : EmailEventType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        if (text.toLowerCase().contains("bounce")) return BOUNCED;
        if (text.toLowerCase().contains("open")) return OPENED;
        if (text.toLowerCase().contains("click")) return CLICKED;
        if (text.toLowerCase().contains("deliver")) return DELIVERED;
        if (text.toLowerCase().contains("complaint") || text.toLowerCase().contains("spam")) return COMPLAINT;
        if (text.toLowerCase().contains("unsub")) return UNSUBSCRIBED;
        return SENT;
    }
}
