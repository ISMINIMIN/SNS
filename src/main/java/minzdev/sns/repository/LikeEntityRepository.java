package minzdev.sns.repository;

import minzdev.sns.model.entity.LikeEntity;
import minzdev.sns.model.entity.PostEntity;
import minzdev.sns.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {

    Optional<LikeEntity> findByUserAndPost(UserEntity userEntity, PostEntity postEntity);

    Integer countByPost(PostEntity postEntity);

}
