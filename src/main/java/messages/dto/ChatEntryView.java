package messages.dto;

import lombok.Builder;
import lombok.Data;
import utils.model.ChatEntry;

import java.io.Serializable;

@Data
@Builder
public class ChatEntryView implements Serializable {
    private final String email;
    private final String message;

    public ChatEntryView(String email, String message) {
        this.email = email;
        this.message = message;
    }

    public static ChatEntryView fromChatEntry(ChatEntry chatEntry) {
        return ChatEntryView.builder()
                .email(chatEntry.getEmail())
                .message(chatEntry.getMessage())
                .build();
    }
}
