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


public class UserGetRestApiTest {

    TestContextApi testContextApi = new TestContextApi();
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
    public void userFetchedOk() {
        User fetchedUser =
                given()
                        .contentType("application/json")
                        .when().get("/user/{userid}", data.getCreditUser().getId())
                        .then().statusCode(200)
                        .extract().response().as(User.class);
        assertThat(fetchedUser).isEqualTo(data.getCreditUser());
    }

    @Test
    public void userNotFound() {
        given()
                .contentType("application/json")
                .when().get("/user/{userid}", "non-existing-user-id")
                .then().statusCode(400);
    }

}
