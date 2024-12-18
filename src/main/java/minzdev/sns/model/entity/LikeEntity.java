package minzdev.sns.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "\"like\"")
@SQLDelete(sql = "UPDATE \"like\" SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at is NULL")
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp deletedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    public static LikeEntity of(UserEntity userEntity, PostEntity postEntity) {
        LikeEntity likeEntity = new LikeEntity();
        likeEntity.setUser(userEntity);
        likeEntity.setPost(postEntity);

        return likeEntity;
    }

}
