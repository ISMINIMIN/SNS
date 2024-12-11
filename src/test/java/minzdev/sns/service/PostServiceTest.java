package minzdev.sns.service;

import minzdev.sns.exception.ErrorCode;
import minzdev.sns.exception.SnsApplicationException;
import minzdev.sns.model.entity.PostEntity;
import minzdev.sns.model.entity.UserEntity;
import minzdev.sns.repository.PostEntityRepository;
import minzdev.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockitoBean
    private PostEntityRepository postEntityRepository;

    @MockitoBean
    private UserEntityRepository userEntityRepository;

    @Test
    void create_post_success() {
        String title = "title";
        String body = "body";
        String username = "username";

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        Assertions.assertDoesNotThrow(() -> postService.create(title, body, username));
    }

    @Test
    @DisplayName("존재하지 않는 이름(username)으로 포스트를 작성할 수 없다.")
    void create_post_error_non_existent_username() {
        String title = "title";
        String body = "body";
        String username = "username";

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        SnsApplicationException exception = Assertions.assertThrows(
                SnsApplicationException.class, () -> postService.create(title, body, username)
        );

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

}
