package test.rest.api.account;

import org.junit.Before;

import static io.restassured.RestAssured.baseURI;

public class AccountRestApiTest {

    @Before
    public void setup() {
        baseURI = "http://localhost:8080/4-Revolut/money-transfer/1.0";
    }

}
