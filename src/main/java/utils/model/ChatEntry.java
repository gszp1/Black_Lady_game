package utils.model;

import lombok.Data;

/**
 * Class for chat entry.
 */
@Data
public class ChatEntry {

    /**
     * Sender's email.
     */
    private final String email;

    /**
     * Message contents.
     */
    private final String message;
}
