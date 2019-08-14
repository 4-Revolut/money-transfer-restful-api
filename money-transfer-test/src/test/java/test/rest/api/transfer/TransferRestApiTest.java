package test.rest.api.transfer;


import io.moneytransfer.model.Account;
import io.moneytransfer.model.AccountArray;
import io.moneytransfer.model.Transfer;
import io.moneytransfer.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.rest.api.sampledata.SampleTestData;
import test.rest.utils.TestContextApi;

import java.math.BigDecimal;

import static io.moneytransfer.constants.MiscConstants.ERROR_RESPONSE_TYPE;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class TransferRestApiTest {

    public static final String TRANSFER_PATH = "/transfer";

    private TestContextApi testContextApi = new TestContextApi();
    private SampleTestData data = new SampleTestData();

    @Before
    public void setup() {
        baseURI = "http://localhost:8080/4-Revolut/money-transfer/1.0";
        testContextApi.clearContext();
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

    @Test
    public void transferFail_debitAccountBalanceTooLow() {

        BigDecimal transferAmount = new BigDecimal(2000);
        given().
                given()
                .contentType("application/json")
                .body(new Transfer(data.getDebitUser().getId(), data.getDebitAccountId(), data.getCreditUser().getId(), data.getCreditAccountId(), transferAmount))
                .when().post(TRANSFER_PATH)
                .then().statusCode(400)
                .body("type", equalTo(ERROR_RESPONSE_TYPE))
                .body("message", equalTo(format("Account:%s does not have enough funds to make transfer", data.getDebitAccountId())));
    }

    @Test
    public void transferFail_creditorAccountBalanceOverLimit() {
        BigDecimal transferAmount = new BigDecimal(5000);

        Account accountA = new Account(null, "accountA", transferAmount);
        AccountArray accountArray = new AccountArray();
        accountArray.add(accountA);
        User userToCreate = new User("onemoreemail@mailbox.com", "somefirstname", "somelastname", accountArray);
        User createdUser =
                given()
                        .contentType("application/json")
                        .body(userToCreate)
                        .when().post("/user")
                        .then().statusCode(200)
                        .extract().response().as(User.class);

        Account accountB = new Account(null, "accountA", new BigDecimal("8000"));
        AccountArray accountArrayB = new AccountArray();
        accountArrayB.add(accountB);
        User userToCreateB = new User("differentemail@mailbox.com", "somefirstname", "somelastname", accountArrayB);
        User createdUserB =
                given()
                        .contentType("application/json")
                        .body(userToCreateB)
                        .when().post("/user")
                        .then().statusCode(200)
                        .extract().response().as(User.class);
        given().
                given()
                .contentType("application/json")
                .body(new Transfer(createdUser.getId(), createdUser.getAccountArray().get(0).getId(), createdUserB.getId(), createdUserB.getAccountArray().get(0).getId(), transferAmount))
                .when().post(TRANSFER_PATH)
                .then().statusCode(400)
                .body("type", equalTo(ERROR_RESPONSE_TYPE))
                .body("message", equalTo(format("Transfer failed, credited amount too big to transfer to account:%s", createdUserB.getAccountArray().get(0).getId())));
    }



    @Test
    public void transferFail_overMaxTransferLimit() {
        BigDecimal transferAmount = new BigDecimal(10000);

        Account accountA = new Account(null, "accountA", transferAmount);
        AccountArray accountArray = new AccountArray();
        accountArray.add(accountA);
        User userToCreate = new User("onemoreemail@mailbox.com", "somefirstname", "somelastname", accountArray);
        User createdUser =
                given()
                        .contentType("application/json")
                        .body(userToCreate)
                        .when().post("/user")
                        .then().statusCode(200)
                        .extract().response().as(User.class);
        given().
                given()
                .contentType("application/json")
                .body(new Transfer(createdUser.getId(), createdUser.getAccountArray().get(0).getId(), data.getCreditUser().getId(), data.getCreditAccountId(), transferAmount))
                .when().post(TRANSFER_PATH)
                .then().statusCode(400)
                .body("type", equalTo(ERROR_RESPONSE_TYPE))
                .body("message", equalTo("max transfer is 5000"));
    }
}
