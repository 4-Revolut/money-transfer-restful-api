package test.rest.api.user;


import io.moneytransfer.model.User;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.oneOf;


public class UserCreateRestApiTest {

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
    public void userCreateFail_noEntity() {
        given()
            .contentType("application/json")
            .when().post("/user")
            .then().statusCode(400);
    }

    @Test
    public void userCreateFail_noMandatoryFields() {
        given()
                .contentType("application/json")
                .body(new User(null, null, null,null))
                .when().post("/user")
                .then()
                    .statusCode(400)
                    .body("type", equalTo("error"))
                    .body("message", equalTo("error"));
//                    .body("message", oneOf("email is mandatory", "firstname is mandatory", "lastname is mandatory"));
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
                .body("message", equalTo("firstname should be between 5 and 10 symbols"));
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
                .body("message", equalTo("firstname should be between 5 and 10 symbols"));
    }

    @Test
    public void userCreateFail_lastnameTooShort() {
        given()
                .contentType("application/json")
                .body(new User("somemail@mailbox.com", "firstname", TOO_SHORT, null))
                .when().post("/user")
                .then().statusCode(400)
                .assertThat()
                .body("type", equalTo("error"))
                .body("message", equalTo("lastname should be between 5 and 10 symbols"));
    }

    @Test
    public void userCreateFail_lastnameTooLong() {
        given()
            .contentType("application/json")
            .body(new User("somemail@mailbox.com", "firstname", TOO_LONG, null))
        .when()
            .post("/user")
        .then().statusCode(400)
            .assertThat()
                .body("type", equalTo("error"))
                .body("message", equalTo("lastname should be between 5 and 10 symbols"));
    }

    @Test
    public void userCreateFail_emailIncorrect() {
        given()
                .contentType("application/json")
                .body(new User("incorrectEmail", "firstname", "lastname", null))
                .when().post("/user")
                .then().statusCode(400)
                .assertThat()
                .body("type", equalTo("error"))
                .body("message", equalTo("please provide correct email"));
    }

    @Test
    public void userCreateFail_emailTooShort() {
        given()
                .contentType("application/json")
                .body(new User("a@b.com", "firstname", "lastname", null))
                .when().post("/user")
                .then().statusCode(400)
                .assertThat()
                .body("type", equalTo("error"))
                .body("message", equalTo("email should be between 10 and 20 symbols"));
    }

    @Test
    public void userCreateFail_emailTooLong() {
        given()
                .contentType("application/json")
                .body(new User("email@thatistoolong.com", "firstname", "lastname", null))
                .when().post("/user")
                .then().statusCode(400)
                .assertThat()
                .body("type", equalTo("error"))
                .body("message", equalTo("email should be between 10 and 20 symbols"));
    }
}
