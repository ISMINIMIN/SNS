package minzdev.sns.service;

import lombok.AllArgsConstructor;
import minzdev.sns.model.dto.Comment;
import minzdev.sns.model.entity.*;
import minzdev.sns.model.enumeration.AlarmType;
import minzdev.sns.repository.AlarmEntityRepository;
import minzdev.sns.repository.CommentEntityRepository;
import minzdev.sns.repository.LikeEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PostDetailService {

    private final UserService userService;
    private final PostService postService;

    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;

    @Transactional
    public void like(Integer postId, String username) {
        UserEntity userEntity = userService.findByUsername(username);
        PostEntity postEntity = postService.findById(postId);

        Optional<LikeEntity> likeEntity = likeEntityRepository.findByUserAndPost(userEntity, postEntity);

        if(likeEntity.isPresent()) {
            likeEntityRepository.delete(likeEntity.get());
        } else {
            likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));
            alarmEntityRepository.save(AlarmEntity.of(
                    postEntity.getUser(), AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())
            ));
        }
    }

    @Transactional
    public int countLike(Integer postId) {
        PostEntity postEntity = postService.findById(postId);
        return likeEntityRepository.countByPost(postEntity);
    }

    @Transactional
    public void createComment(Integer postId, String comment, String username) {
        UserEntity userEntity = userService.findByUsername(username);
        PostEntity postEntity = postService.findById(postId);

        commentEntityRepository.save(CommentEntity.of(userEntity, postEntity, comment));
        alarmEntityRepository.save(AlarmEntity.of(
                postEntity.getUser(), AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())
        ));
    }

    @Transactional(readOnly = true)
    public Page<Comment> getComments(Integer postId, Pageable pageable) {
        PostEntity postEntity = postService.findById(postId);
        return commentEntityRepository.findAllByPost(postEntity, pageable).map(Comment::fromEntity);
    }

}
