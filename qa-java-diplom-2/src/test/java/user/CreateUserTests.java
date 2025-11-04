package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Generators;

import static org.hamcrest.Matchers.*;

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
        accessToken = response.then().extract().path("accessToken").toString();

        // Assert
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
    @DisplayName("Создание двух одинаковых пользователей")
    public void createTwoIdenticalUsersReturnsError() {
        // Arrange

        // Act
        Response response = UserActions.create(user);
        accessToken = response.then().extract().path("accessToken").toString();

        // Assert
        UserActions.create(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @After
    public void tearDown() {
        UserActions.delete(accessToken);
    }
}
