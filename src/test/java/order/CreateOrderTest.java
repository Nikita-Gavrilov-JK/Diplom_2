package order;

import io.restassured.response.ValidatableResponse;
import models.Order;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.OrderStepTest;
import steps.UserStepTest;
import user.UserGenerator;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
public class CreateOrderTest {
    private final UserStepTest userStep = new UserStepTest();
    private final OrderStepTest orderStep = new OrderStepTest();
    private User user;
    private String token;
    private List<String> validIngredients;

    @BeforeEach
    public void setUp() {
        user = UserGenerator.random();
        ValidatableResponse response = userStep.create(user);
        token = response.extract().path("accessToken");

        validIngredients = Arrays.asList(
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa70"
        );
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuthorization() {
        Order order = new Order(validIngredients);
        ValidatableResponse response = orderStep.createOrderWithToken(token, order);

        response.statusCode(200)
                .body("success", is(true))
                .body("order.number", notNullValue());
    }
    @Test
    @DisplayName("Создание заказа без авторизацией")
    public void createOrderNotAuthorization() {
        Order order = new Order(validIngredients);
        ValidatableResponse response = orderStep.createOrderNotToken(order);

        response.statusCode(200)
                .body("success", is(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        Order order = new Order(null);
        ValidatableResponse response = orderStep.createOrderWithToken(token, order);

        response.statusCode(400)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    //Падает с ошибкой со статусом 400 но в документации пишет что при неправильном хеше статус должен быть 500
    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredients() {
        Order order = new Order(Arrays.asList("invalid_hash"));
        ValidatableResponse response = orderStep.createOrderWithToken(token, order);

        response.statusCode(400)
                .body("success", is(false))
                .body("message", notNullValue());
    }

    @AfterEach
    public void tearDown() {
        if (token != null) {
            userStep.delete(token);
        }
    }
}
