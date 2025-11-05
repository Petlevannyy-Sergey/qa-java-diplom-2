package shared;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;

public class Assertions {
    public static void AssertThatRequestThrowsError(
            Response response,
            int status,
            String message) {
        response.then().assertThat().statusCode(status)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo(message));
    }
}
