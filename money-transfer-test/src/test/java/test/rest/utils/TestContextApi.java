package test.rest.utils;

import static io.restassured.RestAssured.given;

public class TestContextApi {

    public void clearContext() {
        given()
            .contentType("application/json")
            .when().get("/test/clearContext")
            .then().statusCode(200);
    }
}
