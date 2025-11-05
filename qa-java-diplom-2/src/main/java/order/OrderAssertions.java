package order;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderAssertions {
    public static void AssertThatOrderCreated(Response response) {
        response
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order", notNullValue())
                .and()
                .body("name", notNullValue())
                .and()
                .body("order.number", notNullValue());
    }
}
