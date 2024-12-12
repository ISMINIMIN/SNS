package minzdev.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import minzdev.sns.model.dto.User;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Integer id;
    private String username;

    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername()
        );
    }

}
