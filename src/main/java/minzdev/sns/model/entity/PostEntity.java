package minzdev.sns.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import minzdev.sns.model.dto.UserRole;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "\"post\"")
@SQLDelete(sql = "UPDATE \"post\" SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at is NULL")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp updatedAt;

    @Column
    private Timestamp deletedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static PostEntity of(String title, String body, UserEntity userEntity) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(title);
        postEntity.setBody(body);
        postEntity.setUser(userEntity);

        return postEntity;
    }

}
