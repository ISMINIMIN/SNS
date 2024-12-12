package minzdev.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import minzdev.sns.model.dto.Post;
import minzdev.sns.model.dto.User;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostResponse {

    private Integer id;
    private  String title;
    private String body;
    private UserResponse user;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                UserResponse.fromUser(post.getUser()),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getDeletedAt()
        );
    }

}
