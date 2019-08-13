package test.rest.api.account;

import io.moneytransfer.model.Account;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class AccountRestApiTest {

    @Before
    public void setup() {
        baseURI = "http://localhost:8080/4-Revolut/money-transfer/1.0";
    }

    @Test
    public void accountCreatedOk() {
        given()
            .contentType("application/json")
            .body(new Account("accountName", new BigDecimal("1000")))
            .when().post("/account")
            .then().statusCode(200);
    }
}
