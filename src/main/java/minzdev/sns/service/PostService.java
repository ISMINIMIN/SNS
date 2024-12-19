package minzdev.sns.service;

import lombok.AllArgsConstructor;
import minzdev.sns.exception.ErrorCode;
import minzdev.sns.exception.SnsApplicationException;
import minzdev.sns.model.dto.Post;
import minzdev.sns.model.entity.PostEntity;
import minzdev.sns.model.entity.UserEntity;
import minzdev.sns.repository.PostEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PostService {

    private final UserService userService;

    private final PostEntityRepository postEntityRepository;

    @Transactional
    public void create(String title, String body, String username) {
        UserEntity userEntity = userService.findByUsername(username);
        postEntityRepository.save(PostEntity.of(title, body, userEntity));
    }

    @Transactional
    public Post update(String title, String body, String username, Integer postId) {
        UserEntity userEntity = userService.findByUsername(username);
        PostEntity postEntity = findById(postId);

        if(postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", username, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(String username, Integer postId) {
        UserEntity userEntity = userService.findByUsername(username);
        PostEntity postEntity = findById(postId);

        if(postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", username, postId));
        }

        postEntityRepository.delete(postEntity);
    }

    @Transactional(readOnly = true)
    public Page<Post> getAll(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<Post> getMy(String username, Pageable pageable) {
        UserEntity userEntity = userService.findByUsername(username);
        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }

    public PostEntity findById(Integer postId) {
        return postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
    }

}
