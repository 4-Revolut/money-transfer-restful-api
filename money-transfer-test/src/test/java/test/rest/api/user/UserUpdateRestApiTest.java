package test.rest.api.user;


import io.moneytransfer.model.User;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class UserUpdateRestApiTest {

    private static final String TOO_SHORT = "four";
    private static final String TOO_LONG = "morethanten";

    @Before
    public void setup() {
        baseURI = "http://localhost:8080/4-Revolut/money-transfer/1.0";
    }

    @Test
    public void userCreatedOk() {
        given()
                .contentType("application/json")
                .body(new User("somemail@mailbox.com", "firstname", "lastname", null))
                .when().post("/user")
                .then().statusCode(200);
    }

    @Test
    public void userCreateFail_firstnameTooShort() {
        given()
                .contentType("application/json")
                .body(new User("somemail@mailbox.com", TOO_SHORT, "lastname", null))
                .when().post("/user")
                .then().statusCode(400)
                .assertThat()
                .body("type", equalTo("error"))
                .body("message", equalTo("firstname should be between 5 and 15 symbols"));
    }

    @Test
    public void userCreateFail_firstnameTooLong() {
        given()
                .contentType("application/json")
                .body(new User("somemail@mailbox.com", TOO_LONG, "lastname", null))
                .when().post("/user")
                .then().statusCode(400)
                .assertThat()
                .body("type", equalTo("error"))
                .body("message", equalTo("firstname should be between 5 and 15 symbols"));
    }
}
