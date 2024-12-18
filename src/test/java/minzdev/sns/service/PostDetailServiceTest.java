package minzdev.sns.service;

import minzdev.sns.exception.ErrorCode;
import minzdev.sns.exception.SnsApplicationException;
import minzdev.sns.model.entity.LikeEntity;
import minzdev.sns.model.entity.PostEntity;
import minzdev.sns.model.entity.UserEntity;
import minzdev.sns.repository.LikeEntityRepository;
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
import static org.mockito.Mockito.*;

@SpringBootTest
public class PostDetailServiceTest {

    @Autowired
    private PostDetailService postDetailService;

    @MockitoBean
    private PostEntityRepository postEntityRepository;

    @MockitoBean
    private UserEntityRepository userEntityRepository;

    @MockitoBean
    private LikeEntityRepository likeEntityRepository;

    @Test
    void post_like_success() {
        Integer postId = 1;
        String username = "username";

        UserEntity userEntity = mock(UserEntity.class);
        PostEntity postEntity = mock(PostEntity.class);

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(likeEntityRepository.findByUserAndPost(userEntity, postEntity)).thenReturn(Optional.empty());

        Assertions.assertDoesNotThrow(() -> postDetailService.like(postId, username));
        verify(likeEntityRepository, times(1)).save(any(LikeEntity.class));
    }

    @Test
    @DisplayName("이미 좋아요한 경우 좋아요가 취소된다.")
    void post_unlike_success() {
        Integer postId = 1;
        String username = "username";

        UserEntity userEntity = mock(UserEntity.class);
        PostEntity postEntity = mock(PostEntity.class);
        LikeEntity likeEntity = mock(LikeEntity.class);

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(likeEntityRepository.findByUserAndPost(userEntity, postEntity)).thenReturn(Optional.of(likeEntity));

        Assertions.assertDoesNotThrow(() -> postDetailService.like(postId, username));
        verify(likeEntityRepository, times(1)).delete(likeEntity);
    }

    @Test
    @DisplayName("존재하지 않는 포스트에 좋아요를 할 수 없다.")
    void post_like_error_non_existent_post() {
        Integer postId = 1;
        String username = "username";

        UserEntity userEntity = mock(UserEntity.class);

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        SnsApplicationException exception = Assertions.assertThrows(
                SnsApplicationException.class, () -> postDetailService.like(postId, username)
        );

        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void count_like_success() {
        Integer postId = 1;
        PostEntity postEntity = mock(PostEntity.class);

        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(likeEntityRepository.countByPost(postEntity)).thenReturn(5);

        int likeCount = postDetailService.countLike(postId);
        Assertions.assertEquals(5, likeCount);
    }

}
