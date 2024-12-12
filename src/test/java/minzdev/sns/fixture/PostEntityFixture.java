package minzdev.sns.fixture;

import minzdev.sns.model.entity.PostEntity;
import minzdev.sns.model.entity.UserEntity;

public class PostEntityFixture {

    public static PostEntity get(String username, Integer postId, Integer userId) {
        UserEntity userEntityFixture = new UserEntity();
        userEntityFixture.setId(userId);
        userEntityFixture.setUsername(username);

        PostEntity postEntityFixture = new PostEntity();
        postEntityFixture.setUser(userEntityFixture);
        postEntityFixture.setId(postId);

        return postEntityFixture;
    }

}
