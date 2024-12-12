package minzdev.sns.service;

import minzdev.sns.exception.ErrorCode;
import minzdev.sns.exception.SnsApplicationException;
import minzdev.sns.fixture.UserEntityFixture;
import minzdev.sns.model.entity.UserEntity;
import minzdev.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserEntityRepository userEntityRepository;

    @MockitoBean
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void join_success() {
        String username = "username";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(username, password, 1);
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(UserEntityFixture.get(username, password, 1));

        Assertions.assertDoesNotThrow(() -> userService.join(username, password));
    }

    @Test
    @DisplayName("이미 존재하는 이름(username)으로 회원가입을 진행할 수 없다.")
    public void join_error_duplicate_username() {
        String username = "username";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(username, password, 1);
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(fixture));
        when(passwordEncoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        SnsApplicationException exception = Assertions.assertThrows(
                SnsApplicationException.class, () -> userService.join(username, password)
        );

        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME, exception.getErrorCode());
    }

    @Test
    void login_success() {
        String username = "username";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(username, password, 1);
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(fixture));
        when(passwordEncoder.matches(password, fixture.getPassword())).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> userService.login(username, password));
    }

    @Test
    @DisplayName("존재하지 않는 이름(username)으로 로그인할 수 없다.")
    public void login_error_non_existent_username() {
        String username = "username";
        String password = "password";

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());

        SnsApplicationException exception = Assertions.assertThrows(
                SnsApplicationException.class, () -> userService.login(username, password)
        );

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("비밀번호가 틀린 경우 로그인할 수 없다.")
    public void login_error_wrong_password() {
        String username = "username";
        String password = "password";
        String wrongPassword = "wrongPassword";

        UserEntity fixture = UserEntityFixture.get(username, password, 1);
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(fixture));

        SnsApplicationException exception = Assertions.assertThrows(
                SnsApplicationException.class, () -> userService.login(username, wrongPassword)
        );

        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }

}
