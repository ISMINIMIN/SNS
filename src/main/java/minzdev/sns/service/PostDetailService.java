package minzdev.sns.service;

import lombok.AllArgsConstructor;
import minzdev.sns.exception.ErrorCode;
import minzdev.sns.exception.SnsApplicationException;
import minzdev.sns.model.entity.LikeEntity;
import minzdev.sns.model.entity.PostEntity;
import minzdev.sns.model.entity.UserEntity;
import minzdev.sns.repository.LikeEntityRepository;
import minzdev.sns.repository.PostEntityRepository;
import minzdev.sns.repository.UserEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PostDetailService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;

    @Transactional
    public void like(Integer postId, String username) {
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        Optional<LikeEntity> likeEntity = likeEntityRepository.findByUserAndPost(userEntity, postEntity);

        if(likeEntity.isPresent()) likeEntityRepository.delete(likeEntity.get());
        else likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));
    }

    @Transactional
    public int countLike(Integer postId) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
        return likeEntityRepository.countByPost(postEntity);
    }

}
