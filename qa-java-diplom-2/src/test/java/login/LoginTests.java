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
        Response response = LoginActions.login(login);

        // Assert
        LoginAssertions.AssertThatLoginIsSuccess(response, user);
    }

    @Test
    @DisplayName("Авторизация при использовании несуществующих данных пользователя")
    public void LoginWithIncorrectDataThrowsError() {
        // Arrange
        Login login = new Login(Generators.getEmail(), Generators.getPassword());

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
