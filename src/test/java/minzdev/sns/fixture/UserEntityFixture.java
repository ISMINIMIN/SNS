package minzdev.sns.fixture;

import minzdev.sns.model.entity.UserEntity;

public class UserEntityFixture {

    public static UserEntity get(String username, String password) {
        UserEntity userEntityFixture = new UserEntity();
        userEntityFixture.setId(1);
        userEntityFixture.setUsername(username);
        userEntityFixture.setPassword(password);

        return userEntityFixture;
    }

}
