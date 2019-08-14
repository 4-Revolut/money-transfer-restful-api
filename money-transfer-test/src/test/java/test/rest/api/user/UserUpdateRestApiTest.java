package test.rest.api.user;


import io.moneytransfer.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.rest.api.sampledata.SampleTestData;
import test.rest.utils.TestContextApi;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


public class UserUpdateRestApiTest {

    private static final String TOO_SHORT = "four";
    private static final String TOO_LONG = "morethanten";
    private static final String USER_PATH = "/user";

    private TestContextApi testContextApi = new TestContextApi();
    private SampleTestData data = new SampleTestData();

    @Before
    public void setup() {
        baseURI = "http://localhost:8080/4-Revolut/money-transfer/1.0";
        data = data.sampleData();
    }

    @After
    public void cleanContext() {
        testContextApi.clearContext();
    }


    @Test
    public void userUpdatedOk() {
        User userUpdate = data.getDebitUser();
        userUpdate.setFirstname("first name edit");
        userUpdate.setLastname("last name edit");
        userUpdate.setEmail("emailedit@email.org");

        User updatedUser = given()
                .contentType("application/json")
                .body(userUpdate)
                .when().put(USER_PATH)
                .then()
                .statusCode(200)
                .extract().response().as(User.class);

        assertThat(userUpdate).isEqualTo(updatedUser);
    }

    @Test
    public void userNotFound() {
        User userUpdate = data.getDebitUser();
        userUpdate.setId("non-existing-user-id");
        userUpdate.setFirstname("first name edit");

        given()
                .contentType("application/json")
                .body(userUpdate)
                .when().put(USER_PATH)
                .then().statusCode(400);
    }
}
