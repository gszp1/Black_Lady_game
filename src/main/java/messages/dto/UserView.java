package messages.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import utils.User;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@Builder
public class UserView implements Serializable {
    private String email;
    private boolean isMe;

    public static UserView fromUser(User user, String loggedInUserId) {
        return UserView.builder()
                .email(user.getEmail())
                .isMe(user.getUserID().equals(loggedInUserId))
                .build();
    }
}
