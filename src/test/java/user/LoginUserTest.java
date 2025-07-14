package user;

import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.UserStepTest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTest {
    private final UserStepTest userClient = new UserStepTest();
    private User user;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        user = UserGenerator.random();
        userClient.create(user);
    }

    @Test
    @DisplayName("Вход под существующим пользователем")
    public void loginCorrectTest() {
        ValidatableResponse response = userClient.login(user);
        response.statusCode(200).body("accessToken", notNullValue());
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Логин с неверным логином")
    public void loginInvalidEmailTest() {
        user.setEmail("errorEmail@example.com");
        ValidatableResponse response = userClient.login(user);
        response.statusCode(401).body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void loginInvalidPasswordTest() {
        user.setPassword("НеправильныйПароль123");
        ValidatableResponse response = userClient.login(user);
        response.statusCode(401).body("message", equalTo("email or password are incorrect"));
    }

    @AfterEach
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }
}
