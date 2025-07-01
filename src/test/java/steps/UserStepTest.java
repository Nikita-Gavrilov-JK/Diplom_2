package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.User;

import static io.restassured.RestAssured.given;

public class UserStepTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/auth";

    @Step("Создание пользователя")
    public ValidatableResponse create(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(BASE_URL + "/register")
                .then();
    }

    @Step("Логин пользователя")
    public ValidatableResponse login(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(BASE_URL + "/login")
                .then();
    }

    @Step("Обновление данных пользователя с авторизацией")
    public ValidatableResponse updateWithAuth(String token, User user) {
        return given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .patch(BASE_URL + "/user")
                .then();
    }

    @Step("Обновление данных пользователя без авторизации")
    public ValidatableResponse updateNotAuth(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .patch(BASE_URL + "/user")
                .then();
    }

    @Step("Удаление пользователя")
    public void delete(String accessToken) {
        given()
                .header("Authorization", accessToken)
                .when()
                .delete(BASE_URL + "/user")
                .then().statusCode(202);
    }
}
