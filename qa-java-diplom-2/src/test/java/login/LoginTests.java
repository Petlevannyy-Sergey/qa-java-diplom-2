package login;

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

import static org.hamcrest.Matchers.*;

public class LoginTests {
    User user;
    Login login;
    String accessToken;
    Response userResponse;

    @Before
    public void setUp() {
        user = Generators.getUser();
        login = new Login(user.getEmail(), user.getPassword());
        userResponse = UserActions.create(user);
    }

    @Test
    @Description("Проверка авторизации пользователя")
    @DisplayName("Успешная авторизация с валидными данными пользователя")
    public void LoginIsSuccess() {
        // Arrange

        // Act
        accessToken = UserActions.getAccessToken(userResponse);
        Response response = LoginActions.login(login);

        // Assert
        LoginAssertions.AssertThatLoginIsSuccess(response, user);
    }

    @Test
    @Description("Проверка авторизации пользователя")
    @DisplayName("Авторизация при использовании несуществующих данных пользователя")
    public void LoginWithIncorrectPasswordThrowsError() {
        // Arrange

        // Act
        Response response = LoginActions.login(login.getEmail(), Generators.getPassword());

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

        // Act
        Response response = LoginActions.login(Generators.getEmail(), login.getPassword());

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
