package minzdev.sns.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import minzdev.sns.model.entity.PostEntity;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Post {

    private Integer id;
    private  String title;
    private String body;
    private User user;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Post fromEntity(PostEntity entity) {
        return new Post(
                entity.getId(),
                entity.getTitle(),
                entity.getBody(),
                User.fromEntity(entity.getUser()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

}
