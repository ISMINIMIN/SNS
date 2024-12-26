package minzdev.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import minzdev.sns.model.dto.User;
import minzdev.sns.model.enumeration.UserRole;

@Getter
@AllArgsConstructor
public class UserJoinResponse {

    private Integer id;
    private String username;
    private UserRole role;

    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }

}
