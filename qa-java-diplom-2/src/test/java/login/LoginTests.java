package login;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserActions;
import utils.Generators;

import static org.hamcrest.Matchers.*;

public class LoginTests {
    User user;
    String accessToken;

    @Before
    public void setUp() {
        user = Generators.getUser();
    }

    @Test
    @DisplayName("Успешная авторизация с валидными данными пользователя")
    public void LoginIsSuccess() {
        // Arrange
        Login login = new Login(user.getEmail(), user.getPassword());

        // Act
        Response userResponse = UserActions.create(user);
        accessToken = UserActions.getAccessToken(userResponse);

        // Assert
        Response response = LoginActions.login(login);
        response.then().assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user.email", equalTo(user.getEmail()))
                .and()
                .body("user.name", equalTo(user.getName()))
                .and()
                .body("accessToken", notNullValue())
                .and()
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Авторизация при использовании несуществующих данных пользователя")
    public void LoginWithIncorrectDataThrowsError() {
        // Arrange
        Login login = new Login(Generators.getEmail(), Generators.getPassword());

        // Act
        Response response = LoginActions.login(login);

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        if (accessToken != null)
            UserActions.delete(accessToken);
    }
}
