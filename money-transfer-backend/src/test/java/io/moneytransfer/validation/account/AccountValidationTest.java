package io.moneytransfer.validation.account;

import io.moneytransfer.api.ApiResponse;
import io.moneytransfer.model.Account;
import io.moneytransfer.model.User;
import io.moneytransfer.sampledata.AccountSampleData;
import io.moneytransfer.sampledata.UserSampleData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;


@RunWith(MockitoJUnitRunner.class)
public class AccountValidationTest {

    private AccountValidation accountValidation = new AccountValidation();

    UserSampleData userSampleData = new UserSampleData();
    AccountSampleData accountSampleData = new AccountSampleData();


    @Test
    public void validatePass() {
        User user = userSampleData.existingUserWithPromoAccount();
        accountValidation.validate(user);
    }

    @Test
    public void validateFail_moreThanOneAccount() {
        User user = userSampleData.existingUserWithPromoAccount();
        Account anotherAccount = accountSampleData.promoAccount();
        user.getAccountArray().add(anotherAccount);

        try {
            accountValidation.validate(user);
            fail("Expected but not thrown: WebApplicationException");
        } catch (WebApplicationException expectedException) {
            ApiResponse apiResponse = (ApiResponse) expectedException.getResponse().getEntity();
            assertThat(apiResponse.getMessage()).isEqualTo("User can be created with one account only");
        }
    }

    @Test
    public void validateFail_constraintViolation() {
        User user = userSampleData.existingUserWithPromoAccount();
        user.getAccountArray().get(0).setBalance(new BigDecimal("12345"));
        try {
            accountValidation.validate(user);
            fail("Expected but not thrown: WebApplicationException");
        } catch (WebApplicationException expectedException) {
            ApiResponse apiResponse = (ApiResponse) expectedException.getResponse().getEntity();
            assertThat(apiResponse.getMessage()).isNotEmpty();
            assertThat(apiResponse.getMessage()).isNotEqualTo("User can be created with one account only");
        }
    }
}