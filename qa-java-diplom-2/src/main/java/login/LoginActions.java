package login;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.Specification;
import utils.URIs;

import static io.restassured.RestAssured.given;

public class LoginActions {
    @Step("Авторизация пользователя")
    public static Response login(Login login) {
        return given()
                .spec(Specification.requestSpecification())
                .and()
                .body(login)
                .when()
                .post(URIs.LOGIN);
    }
}
