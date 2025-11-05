package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserActions;
import utils.Generators;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class OrderTests {
    User user;
    Order order;
    String accessToken;
    List<String> allIngredients;
    List<String> ingredients;

    @Before
    public void setUp() {
        user = Generators.getUser();
        Response response = UserActions.create(user);
        accessToken = UserActions.getAccessToken(response);
        allIngredients = OrderActions.getAllIngredients();
        ingredients = allIngredients.subList(0, 10);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами авторизированным пользователем")
    public void createOrderWithIngredientsByAuthorizedUserIsSuccess() {
        // Arrange
        order = new Order(ingredients);

        // Act
        Response response = OrderActions.createWithoutAuth(order);

        // Assert
        response
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order", notNullValue())
                .and()
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов авторизированным пользователем")
    public void createOrderWithoutIngredientsByAuthorizedUserThrowsError() {
        // Arrange
        order = new Order(List.of());

        // Act
        Response response = OrderActions.create(accessToken, order);

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа авторизированным пользователем с неверным хешем ингредиента")
    public void createOrderWithInvalidHashByAuthorizedUserThrowsError() {
        // Arrange
        order = new Order(List.of(Generators.getHash()));

        // Act
        Response response = OrderActions.create(accessToken, order);

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Создание заказа не авторизированным пользователем с неверным хешем ингредиента")
    public void createOrderWithInvalidHashByUnauthorizedUserThrowsError() {
        // Arrange
        order = new Order(List.of(Generators.getHash()));

        // Act
        Response response = OrderActions.createWithoutAuth(order);

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами не авторизированным пользователем")
    public void createOrderWithIngredientsByUnauthorizedUserIsSuccess() {
        // Arrange
        order = new Order(ingredients);

        // Act
        Response response = OrderActions.create(accessToken, order);

        // Assert
        response
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("name", notNullValue())
                .and()
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов не авторизированным пользователем")
    public void createOrderWithoutIngredientsByUnauthorizedUserThrowsError() {
        // Arrange
        order = new Order(List.of());

        // Act
        Response response = OrderActions.create(accessToken, order);

        // Assert
        response
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @After
    public void tearDown() {
        if (accessToken != null)
            UserActions.delete(accessToken);
    }
}
