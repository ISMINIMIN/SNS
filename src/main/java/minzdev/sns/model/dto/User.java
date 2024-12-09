package minzdev.sns.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import minzdev.sns.model.entity.UserEntity;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class User {

    private Integer id;
    private UserRole role;
    private String username;
    private String password;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static User fromEntity(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getRole(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

}
