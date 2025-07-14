package order;

import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.OrderStepTest;
import steps.UserStepTest;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
public class GetUserOrdersTest {
    private OrderStepTest orderStep;
    private UserStepTest userStep;
    private String token;
    @BeforeEach
    public void setUp() {
        orderStep = new OrderStepTest();
        userStep = new UserStepTest();

        String randomEmail = "user" + UUID.randomUUID() + "@test.com";
        User user = new User(randomEmail, "password123", "TestUser");
        userStep.create(user).statusCode(200);
        token = userStep.login(user)
                .statusCode(200)
                .extract()
                .path("accessToken");

    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrdersWithAuthorization() {
        ValidatableResponse response = orderStep.getOrders(token);
        response.statusCode(200)
                .body("success", is(true))
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов без авторизации")
    public void getOrdersWithoutAuthorization() {
        ValidatableResponse response = orderStep.getOrdersNotToken();
        response.statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }

    @AfterEach
    public void tearDown() {
        if (token != null) {
            userStep.delete(token);
        }
    }
}
