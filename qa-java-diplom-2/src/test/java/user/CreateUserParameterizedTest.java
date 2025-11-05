package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import shared.Assertions;
import utils.Generators;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class CreateUserParameterizedTest {
    private final String email;
    private final String password;
    private final String name;

    public CreateUserParameterizedTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {"", Generators.getPassword(), Generators.getName()},
                {Generators.getEmail(), "", Generators.getName()},
                {Generators.getEmail(), Generators.getPassword(), ""},
                {"", "", ""}
        };
    }

    @Test
    @DisplayName("Создание пользователя c одним не заполненным обязательным полем")
    public void createUserWithInsufficientDataThrowsError() {
        // Arrange
        User user = new User(email, password, name);

        // Act
        Response response = UserActions.create(user);

        // Assert
        Assertions.AssertThatRequestThrowsError(
                response,
                HttpStatus.SC_FORBIDDEN,
                "Email, password and name are required fields");
    }
}
