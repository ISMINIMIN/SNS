package minzdev.sns.controller;

import lombok.AllArgsConstructor;
import minzdev.sns.controller.request.UserJoinRequest;
import minzdev.sns.controller.request.UserLoginRequest;
import minzdev.sns.controller.response.Response;
import minzdev.sns.controller.response.UserJoinResponse;
import minzdev.sns.controller.response.UserLoginResponse;
import minzdev.sns.model.dto.User;
import minzdev.sns.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getUsername(), request.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getUsername(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

}
