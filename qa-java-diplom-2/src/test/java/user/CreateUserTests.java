package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import shared.Assertions;
import utils.Generators;

public class CreateUserTests {
    User user;
    String accessToken;

    @Before
    public void setUp() {
        user = Generators.getUser();
    }

    @Test
    @DisplayName("Создание пользователя с использованием валидных данных")
    public void createNewUserIsSuccess() {
        // Arrange

        // Act
        Response response = UserActions.create(user);
        accessToken = UserActions.getAccessToken(response);

        // Assert
        UserAssertions.AssertThatUserCreated(response, user);
    }

    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    public void createTwoIdenticalUsersReturnsError() {
        // Arrange

        // Act
        Response firstResponse = UserActions.create(user);
        accessToken = UserActions.getAccessToken(firstResponse);
        Response secondResponse = UserActions.create(user);

        // Assert
        Assertions.AssertThatRequestThrowsError(
                secondResponse,
                HttpStatus.SC_FORBIDDEN,
                "User already exists");
    }

    @After
    public void tearDown() {
        if (accessToken != null)
            UserActions.delete(accessToken);
    }
}
