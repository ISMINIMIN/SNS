package minzdev.sns.fixture;

import minzdev.sns.model.entity.UserEntity;

public class UserEntityFixture {

    public static UserEntity get(String username, String password, Integer userId) {
        UserEntity userEntityFixture = new UserEntity();
        userEntityFixture.setId(userId);
        userEntityFixture.setUsername(username);
        userEntityFixture.setPassword(password);

        return userEntityFixture;
    }

}
