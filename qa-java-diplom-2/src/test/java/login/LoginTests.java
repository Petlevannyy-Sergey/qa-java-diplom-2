package login;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import shared.Assertions;
import user.User;
import user.UserActions;
import utils.Generators;

public class LoginTests {
    User user;
    String accessToken;
    Response userResponse;

    @Before
    public void setUp() {
        user = Generators.getUser();
        userResponse = UserActions.create(user);
        accessToken = UserActions.getAccessToken(userResponse);
    }

    @Test
    @Description("Проверка авторизации пользователя")
    @DisplayName("Успешная авторизация с валидными данными пользователя")
    public void LoginIsSuccess() {
        // Arrange
        Login login = new Login(user.getEmail(), user.getPassword());

        // Act
        Response response = LoginActions.login(login);

        // Assert
        LoginAssertions.AssertThatLoginIsSuccess(response, user);
    }

    @Test
    @Description("Проверка авторизации пользователя")
    @DisplayName("Авторизация при использовании несуществующих данных пользователя")
    public void LoginWithIncorrectPasswordThrowsError() {
        // Arrange
        Login login = new Login(user.getEmail(), Generators.getPassword());

        // Act
        Response response = LoginActions.login(login);

        // Assert
        Assertions.AssertThatRequestThrowsError(
                response,
                HttpStatus.SC_UNAUTHORIZED,
                "email or password are incorrect");
    }

    @Test
    @Description("Проверка авторизации пользователя")
    @DisplayName("Авторизация при использовании несуществующих данных пользователя")
    public void LoginWithIncorrectEmailThrowsError() {
        // Arrange
        Login login = new Login(Generators.getEmail(), user.getPassword());

        // Act
        Response response = LoginActions.login(login);

        // Assert
        Assertions.AssertThatRequestThrowsError(
                response,
                HttpStatus.SC_UNAUTHORIZED,
                "email or password are incorrect");
    }

    @After
    public void tearDown() {
        if (accessToken != null)
            UserActions.delete(accessToken);
    }
}
