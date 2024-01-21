package messages.dto;

import lombok.Builder;
import lombok.Data;
import utils.model.ChatEntry;

import java.io.Serializable;

/**
 * Data transfer object for chat entry.
 */
@Data
@Builder
public class ChatEntryView implements Serializable {
    private final String email;
    private final String message;

    /**
     * Constructor sets email and message.
     * @param email Entry email.
     * @param message Entry message.
     */
    public ChatEntryView(String email, String message) {
        this.email = email;
        this.message = message;
    }

    /**
     * Creates data transfer object using chat entry.
     * @param chatEntry Chat entry.
     * @return Chat entry data transfer object.
     */
    public static ChatEntryView fromChatEntry(ChatEntry chatEntry) {
        return ChatEntryView.builder()
                .email(chatEntry.getEmail())
                .message(chatEntry.getMessage())
                .build();
    }
}
