package minzdev.sns.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import minzdev.sns.model.entity.CommentEntity;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Comment {

    private Integer id;
    private String comment;
    private String username;
    private Integer postId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Comment fromEntity(CommentEntity entity) {
        return new Comment(
                entity.getId(),
                entity.getComment(),
                entity.getUser().getUsername(),
                entity.getPost().getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

}
