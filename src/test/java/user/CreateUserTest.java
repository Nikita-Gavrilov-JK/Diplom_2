package user;

import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.UserStepTest;

import static org.hamcrest.Matchers.*;

public class CreateUserTest {
    private final UserStepTest userStep = new UserStepTest();
    private User user;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        user = UserGenerator.random();
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUser() {
        ValidatableResponse response = userStep.create(user);
        response.statusCode(200).body("success", is(true));
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createDuplicateUser() {
        userStep.create(user);
        ValidatableResponse response = userStep.create(user);
        response.statusCode(403).body("message", containsString("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без почты")
    public void createUserWithoutEmail() {
        user.setEmail(null);
        ValidatableResponse response = userStep.create(user);
        response.statusCode(403).body("message", notNullValue());
    }
    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPassword() {
        user.setPassword(null);
        ValidatableResponse response = userStep.create(user);
        response.statusCode(403)
                .body("message", notNullValue());
    }

    @AfterEach
    public void tearDown() {
        if (accessToken != null) {
            userStep.delete(accessToken);
        }
    }
}
