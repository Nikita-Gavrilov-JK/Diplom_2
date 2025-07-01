package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderStepTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/orders";

    @Step("Создание заказа c авторизацией")
    public ValidatableResponse createOrderWithToken(String token, Order order) {
        return given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(BASE_URL)
                .then();
    }
    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderNotToken(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(BASE_URL)
                .then();
    }

    @Step("Получение заказов пользователя")
    public ValidatableResponse getOrders(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .get(BASE_URL)
                .then();
    }

    @Step("Получение заказов без авторизации")
    public ValidatableResponse getOrdersNotToken() {
        return given()
                .when()
                .get(BASE_URL)
                .then();
    }
}
