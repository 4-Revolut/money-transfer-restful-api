package test.rest.api.transfer;


import io.moneytransfer.model.Transfer;
import io.moneytransfer.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.rest.api.sampledata.SampleTestData;
import test.rest.utils.TestContextApi;

import java.math.BigDecimal;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


public class TransferRestApiTest {

    public static final String TRANSFER_PATH = "/transfer";

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
    public void transferCompletedOk() {

        BigDecimal transferAmount = new BigDecimal(250);
        given().
                given()
                .contentType("application/json")
                .body(new Transfer(data.getDebitUser().getId(), data.getDebitAccountId(), data.getCreditUser().getId(), data.getCreditAccountId(), transferAmount))
                .when().post(TRANSFER_PATH)
                .then().statusCode(200);

        User debitedUser =
                given()
                        .contentType("application/json")
                        .when().get("/user/{userid}", data.getDebitUser().getId())
                        .then().statusCode(200)
                        .extract().response().as(User.class);
        assertThat(debitedUser).isEqualTo(data.getDebitUser());
        assertThat(debitedUser.getAccountArray().get(0).getBalance()).isEqualTo(new BigDecimal(1000).subtract(transferAmount));

        User creditedUser =
                given()
                        .contentType("application/json")
                        .when().get("/user/{userid}", data.getCreditUser().getId())
                        .then().statusCode(200)
                        .extract().response().as(User.class);
        assertThat(creditedUser).isEqualTo(data.getCreditUser());
        assertThat(creditedUser.getAccountArray().get(0).getBalance()).isEqualTo(new BigDecimal(1000).add(transferAmount));
    }


}
