package minzdev.sns.service;

import minzdev.sns.exception.ErrorCode;
import minzdev.sns.exception.SnsApplicationException;
import minzdev.sns.fixture.PostEntityFixture;
import minzdev.sns.fixture.UserEntityFixture;
import minzdev.sns.model.entity.PostEntity;
import minzdev.sns.model.entity.UserEntity;
import minzdev.sns.repository.PostEntityRepository;
import minzdev.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Test
    void update_post_success() {
        String title = "title";
        String body = "body";
        String username = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(username, postId, 1);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(postEntityRepository.saveAndFlush(any())).thenReturn(postEntity);

        Assertions.assertDoesNotThrow(() -> postService.update(title, body, username, postId));
    }

    @Test
    @DisplayName("본인이 작성하지 않은 포스트를 수정할 수 없다.")
    void update_post_error_not_owner() {
        String title = "title";
        String body = "body";
        String username = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(username, postId, 1);
        UserEntity owner = UserEntityFixture.get("owner-un", "owner-pw", 2);

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(owner));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        SnsApplicationException exception = Assertions.assertThrows(
                SnsApplicationException.class, () -> postService.update(title, body, username, postId)
        );

        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    @DisplayName("존재하지 않는 포스트를 수정할 수 없다.")
    void update_post_error_non_existent_post() {
        String title = "title";
        String body = "body";
        String username = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(username, postId, 1);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        SnsApplicationException exception = Assertions.assertThrows(
                SnsApplicationException.class, () -> postService.update(title, body, username, postId)
        );

        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void delete_post_success() {
        String username = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(username, postId, 1);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        Assertions.assertDoesNotThrow(() -> postService.delete(username, postId));
    }

    @Test
    @DisplayName("본인이 작성하지 않은 포스트를 수정할 수 없다.")
    void delete_post_error_not_owner() {
        String username = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(username, postId, 1);
        UserEntity owner = UserEntityFixture.get("owner-un", "owner-pw", 2);

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(owner));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        SnsApplicationException exception = Assertions.assertThrows(
                SnsApplicationException.class, () -> postService.delete(username, postId)
        );

        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    @DisplayName("존재하지 않는 포스트를 수정할 수 없다.")
    void delete_post_error_non_existent_post() {
        String username = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(username, postId, 1);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        SnsApplicationException exception = Assertions.assertThrows(
                SnsApplicationException.class, () -> postService.delete(username, postId)
        );

        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void get_all_post_success() {
        Pageable pageable = mock(Pageable.class);

        when(postEntityRepository.findAll(pageable)).thenReturn(Page.empty());
        Assertions.assertDoesNotThrow(() -> postService.getAll(pageable));
    }

    @Test
    void get_my_post_success() {
        UserEntity userEntity = mock(UserEntity.class);
        Pageable pageable = mock(Pageable.class);

        when(userEntityRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findAllByUser(userEntity, pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> postService.getMy(userEntity.getUsername(), pageable));
    }

}
