package user;

import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.UserStepTest;

import static org.hamcrest.Matchers.*;
public class UpdateUserTest {
    private final UserStepTest userStep = new UserStepTest();
    private User originalUser;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        originalUser = UserGenerator.random();
        userStep.create(originalUser);
        ValidatableResponse loginResponse = userStep.login(originalUser);
        accessToken = loginResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void updateUserWithAuth() {
        User updatedUser = new User("new_" + originalUser.getEmail(), originalUser.getPassword(), "UpdatedName");

        ValidatableResponse response = userStep.updateWithAuth(accessToken, updatedUser);
        response.statusCode(200).body("success", is(true));
        ValidatableResponse loginResponse = userStep.login(updatedUser);
        loginResponse.statusCode(200).body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void updateUserNotAuth() {
        User updatedUser = new User("unauthorized@example.com", "123456", "NoAuth");

        ValidatableResponse response = userStep.updateNotAuth(updatedUser);
        response.statusCode(401).body("message", equalTo("You should be authorised"));
    }
    @AfterEach
    public void tearDown() {
        if (accessToken != null) {
            userStep.delete(accessToken);
        }
    }
}
