package user;

import models.User;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {
    public static User random() {
        String email = RandomStringUtils.randomAlphabetic(8) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(10);
        String name = RandomStringUtils.randomAlphabetic(6);
        return new User(email, password, name);
    }
}
