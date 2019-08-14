package test.rest.api.user;


import io.moneytransfer.model.Account;
import io.moneytransfer.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.rest.utils.TestContextApi;

import java.math.BigDecimal;

import static io.moneytransfer.constants.MiscConstants.ERROR_RESPONSE_TYPE;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.oneOf;


public class UserCreateRestApiTest {

    private static final String STRING_4 = "four";
    private static final String STRING_50_STRONG = "CRRSxie5im3hq6bXvwoaIn6Pz6OGj2vWy1lHecc9e9RCEoS13z";
    private static final String USER_PATH = "/user";
    public static final int UUID_LENGTH = 36;

    private TestContextApi testContextApi = new TestContextApi();

    @Before
    public void setup() {
        baseURI = "http://localhost:8080/4-Revolut/money-transfer/1.0";
    }

    @After
    public void clearContext() {
        testContextApi.clearContext();
    }

    @Test
    public void userCreatedOkWithPromoAccount() {
        testContextApi.clearContext();
        User userToCreate = new User("somemail@mailbox.com", "firstname", "lastname", null);
        User createdUser =
                given()
                        .contentType("application/json")
                        .body(userToCreate)
                        .when().post(USER_PATH)
                        .then().statusCode(200)
                        .extract().response().as(User.class);
        assertThat(createdUser).isEqualToIgnoringGivenFields(userToCreate, "id", "accountArray");
        assertThat(createdUser.getId()).isNotBlank();
        assertThat(createdUser.getId().length()).isEqualTo(UUID_LENGTH);
        assertThat(createdUser.getAccountArray().get(0)).isEqualToIgnoringGivenFields(promoAccount(), "id");

        User fetchedUser =
                given()
                        .contentType("application/json")
                        .when().get("/user/{userid}", createdUser.getId())
                        .then().statusCode(200)
                        .extract().response().as(User.class);
        assertThat(fetchedUser).isEqualTo(createdUser);
    }

    @Test
    public void userCreateFail_duplicateUserEmail() {
        given()
                .contentType("application/json")
                .body(new User("somemail@mailbox.com", "firstname", "lastname", null))
                .when().post(USER_PATH)
                .then()
                .statusCode(200);

        given()
                .contentType("application/json")
                .body(new User("somemail@mailbox.com", "firstname", "lastname", null))
                .when().post(USER_PATH)
                .then()
                .statusCode(409)
                .body("type", equalTo(ERROR_RESPONSE_TYPE))
                .body("message", equalTo("User with provided email already exists"));
    }

    @Test
    public void userCreateFail_noEntity() {
        given()
            .contentType("application/json")
            .when().post(USER_PATH)
            .then()
                .statusCode(400)
                .body("type", equalTo(ERROR_RESPONSE_TYPE))
                .body("message", equalTo("Provide new User information as json"));
    }

    @Test
    public void userCreateFail_noMandatoryFields() {
        given()
                .contentType("application/json")
                .body(new User(null, null, null,null))
                .when().post(USER_PATH)
                .then()
                    .statusCode(400)
                    .body("type", equalTo(ERROR_RESPONSE_TYPE))
                    .body("message", oneOf("email is mandatory", "firstname is mandatory", "lastname is mandatory"));
    }

    @Test
    public void userCreateFail_firstnameTooShort() {
        given()
                .contentType("application/json")
                .body(new User("somemail@mailbox.com", STRING_4, "lastname", null))
                .when().post(USER_PATH)
                .then().statusCode(400)
                .assertThat()
                .body("type", equalTo(ERROR_RESPONSE_TYPE))
                .body("message", equalTo("firstname should be between 5 and 15 symbols"));
    }

    @Test
    public void userCreateFail_firstnameTooLong() {
        given()
                .contentType("application/json")
                .body(new User("somemail@mailbox.com", STRING_50_STRONG, "lastname", null))
                .when().post(USER_PATH)
                .then().statusCode(400)
                .assertThat()
                .body("type", equalTo(ERROR_RESPONSE_TYPE))
                .body("message", equalTo("firstname should be between 5 and 15 symbols"));
    }

    @Test
    public void userCreateFail_lastnameTooShort() {
        given()
                .contentType("application/json")
                .body(new User("somemail@mailbox.com", "firstname", STRING_4, null))
                .when().post(USER_PATH)
                .then().statusCode(400)
                .assertThat()
                .body("type", equalTo(ERROR_RESPONSE_TYPE))
                .body("message", equalTo("lastname should be between 5 and 15 symbols"));
    }

    @Test
    public void userCreateFail_lastnameTooLong() {
        given()
            .contentType("application/json")
            .body(new User("somemail@mailbox.com", "firstname", STRING_50_STRONG, null))
        .when()
            .post(USER_PATH)
        .then().statusCode(400)
            .assertThat()
                .body("type", equalTo(ERROR_RESPONSE_TYPE))
                .body("message", equalTo("lastname should be between 5 and 15 symbols"));
    }

    @Test
    public void userCreateFail_emailIncorrect() {
        given()
                .contentType("application/json")
                .body(new User("incorrectEmail", "firstname", "lastname", null))
                .when().post(USER_PATH)
                .then().statusCode(400)
                .assertThat()
                .body("type", equalTo(ERROR_RESPONSE_TYPE))
                .body("message", equalTo("please provide correct email"));
    }

    @Test
    public void userCreateFail_emailTooShort() {
        given()
                .contentType("application/json")
                .body(new User("a@b.com", "firstname", "lastname", null))
                .when().post(USER_PATH)
                .then().statusCode(400)
                .assertThat()
                .body("type", equalTo(ERROR_RESPONSE_TYPE))
                .body("message", equalTo("email should be between 10 and 30 symbols"));
    }

    @Test
    public void userCreateFail_emailTooLong() {
        given()
                .contentType("application/json")
                .body(new User("emailtoolong@toolongtoolong.com", "firstname", "lastname", null))
                .when().post(USER_PATH)
                .then().statusCode(400)
                .assertThat()
                .body("type", equalTo(ERROR_RESPONSE_TYPE))
                .body("message", equalTo("email should be between 10 and 30 symbols"));
    }

    private Account promoAccount() {
        return new Account(null, "promo-account", new BigDecimal("1000"));
    }
}
