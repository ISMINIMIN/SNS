package minzdev.sns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import minzdev.sns.controller.request.UserJoinRequest;
import minzdev.sns.controller.request.UserLoginRequest;
import minzdev.sns.exception.ErrorCode;
import minzdev.sns.exception.SnsApplicationException;
import minzdev.sns.model.dto.User;
import minzdev.sns.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    public void join_success() throws Exception {
        String username = "username";
        String password = "password";

        when(userService.join(username, password)).thenReturn(mock(User.class));

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(username, password))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이미 존재하는 이름(username)으로 회원가입을 진행할 수 없다.")
    public void join_error_duplicate_username() throws Exception {
        String username = "username";
        String password = "password";

        when(userService.join(username, password)).thenThrow(
                new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME)
        );

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(username, password))))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void login_success() throws Exception {
        String username = "username";
        String password = "password";

        when(userService.login(username, password)).thenReturn("token");

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(username, password))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("존재하지 않는 이름(username)으로 로그인할 수 없다.")
    public void login_error_non_existent_username() throws Exception {
        String username = "username";
        String password = "password";

        when(userService.login(username, password)).thenThrow(
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND)
        );

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(username, password))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("비밀번호가 틀린 경우 로그인할 수 없다.")
    public void login_error_wrong_password() throws Exception {
        String username = "username";
        String password = "password";

        when(userService.login(username, password)).thenThrow(
                new SnsApplicationException(ErrorCode.INVALID_PASSWORD)
        );

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(username, password))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}
