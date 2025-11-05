package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import config.Specification;
import config.URIs;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderActions {
    @Step("Создание заказа с авторизацией")
    public static Response create(String accessToken, Order order) {
        return given()
                .spec(Specification.requestSpecification())
                .and()
                .header("Authorization", accessToken)
                .and()
                .body(order)
                .when()
                .post(URIs.ORDER);
    }

    @Step("Создание заказа без авторизации")
    public static Response createWithoutAuth(Order order) {
        return given()
                .spec(Specification.requestSpecification())
                .and()
                .body(order)
                .when()
                .post(URIs.ORDER);
    }

    @Step("Получение списка всех ингредиентов")
    public static List<String> getAllIngredients() {
        return given()
                .spec(Specification.requestSpecification())
                .when()
                .get(URIs.INGREDIENTS)
                .then()
                .extract()
                .path("data._id");
    }

}
