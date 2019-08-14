package test.rest.api.sampledata;

import io.moneytransfer.model.User;

import static io.restassured.RestAssured.given;

public class SampleTestData {

    private User debitUser;
    private String debitAccountId;
    private User creditUser;
    private String creditAccountId;

    public SampleTestData sampleData() {
        SampleTestData sampleTestData = new SampleTestData();
        someUser(sampleTestData);
        otherUser(sampleTestData);
        return sampleTestData;
    }

    private SampleTestData someUser(SampleTestData sampleTestData) {
        User userToCreate = new User("somemail@mailbox.com", "somefirstname", "somelastname", null);
        User createdUser =
                given()
                        .contentType("application/json")
                        .body(userToCreate)
                        .when().post("/user")
                        .then().statusCode(200)
                        .extract().response().as(User.class);

        sampleTestData.setDebitUser(createdUser);
        sampleTestData.setDebitAccountId(createdUser.getAccountArray().get(0).getId());
        return sampleTestData;
    }

    private SampleTestData otherUser(SampleTestData sampleTestData) {
        User userToCreate = new User("othermail@mailbox.com", "otherfirstname", "otherlastname", null);
        User createdUser =
                given()
                        .contentType("application/json")
                        .body(userToCreate)
                        .when().post("/user")
                        .then().statusCode(200)
                        .extract().response().as(User.class);

        sampleTestData.setCreditUser(createdUser);
        sampleTestData.setCreditAccountId(createdUser.getAccountArray().get(0).getId());
        return sampleTestData;
    }

    public User getDebitUser() {
        return debitUser;
    }

    public void setDebitUser(User debitUser) {
        this.debitUser = debitUser;
    }

    public String getDebitAccountId() {
        return debitAccountId;
    }

    public void setDebitAccountId(String debitAccountId) {
        this.debitAccountId = debitAccountId;
    }

    public User getCreditUser() {
        return creditUser;
    }

    public void setCreditUser(User creditUser) {
        this.creditUser = creditUser;
    }

    public String getCreditAccountId() {
        return creditAccountId;
    }

    public void setCreditAccountId(String creditAccountId) {
        this.creditAccountId = creditAccountId;
    }
}
