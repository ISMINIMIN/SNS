package minzdev.sns.controller;

import lombok.AllArgsConstructor;
import minzdev.sns.controller.request.UserJoinRequest;
import minzdev.sns.controller.request.UserLoginRequest;
import minzdev.sns.controller.response.AlarmResponse;
import minzdev.sns.controller.response.Response;
import minzdev.sns.controller.response.UserJoinResponse;
import minzdev.sns.controller.response.UserLoginResponse;
import minzdev.sns.model.dto.User;
import minzdev.sns.service.AlarmService;
import minzdev.sns.service.UserService;
import minzdev.sns.util.AuthUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final AlarmService alarmService;

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

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> getAllAlarm(Pageable pageable, Authentication auth) {
        User user = AuthUtils.getUser(auth);
        return Response.success(userService.getAllAlarm(user.getId(), pageable).map(AlarmResponse::from));
    }

    @GetMapping("/alarm/subscribe")
    public SseEmitter subscribe(Authentication auth) {
        User user = AuthUtils.getUser(auth);
        return alarmService.connect(user.getId());
    }

}
