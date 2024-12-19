package minzdev.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import minzdev.sns.model.dto.Comment;
import minzdev.sns.model.dto.Post;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private Integer id;
    private String comment;
    private String username;
    private Integer postId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getComment(),
                comment.getUsername(),
                comment.getPostId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getDeletedAt()
        );
    }

}
