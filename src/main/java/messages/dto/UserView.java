package messages.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import utils.User;

import java.io.Serializable;

/**
 *  Data transfer object for representing user information.
 */
@Data
@EqualsAndHashCode
@Builder
public class UserView implements Serializable {
    /**
     * User's email.
     */
    private String email;

    /**
     * Boolean stating if data belong to the user who receives data.
     */
    private boolean isMe;

    /**
     * Creates a UserView instance from a User object.
     * @param user User data.
     * @param loggedInUserId User's ID.
     * @return UserView instance.
     */
    public static UserView fromUser(User user, String loggedInUserId) {
        return UserView.builder()
                .email(user.getEmail())
                .isMe(user.getUserID().equals(loggedInUserId))
                .build();
    }
}
