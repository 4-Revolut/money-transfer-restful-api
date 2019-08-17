package io.moneytransfer.validation.transfer;

import io.moneytransfer.api.ApiResponse;
import io.moneytransfer.model.Account;
import io.moneytransfer.sampledata.AccountSampleData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;
import java.math.BigDecimal;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;


@RunWith(MockitoJUnitRunner.class)
public class BalanceValidationTest {

    private BalanceValidation balanceValidation = new BalanceValidation();

    private AccountSampleData accountSampleData = new AccountSampleData();

    @Test
    public void validateDebit_Ok() {
        Account promoAccount = accountSampleData.promoAccount();
        balanceValidation.validateDebit(promoAccount, new BigDecimal("500"));
    }

    @Test
    public void validateDebitFail_BalanceTooLow() {
        Account debitAccount = accountSampleData.promoAccount();
        debitAccount.setId("debitAccount");
        try {
            balanceValidation.validateDebit(debitAccount, new BigDecimal("2000"));
            fail("Expected but not thrown: WebApplicationException");
        } catch (WebApplicationException expectedException) {
            ApiResponse apiResponse = (ApiResponse) expectedException.getResponse().getEntity();
            assertThat(apiResponse.getMessage()).isEqualTo(format("Account:%s does not have enough funds to make transfer", debitAccount.getId()));
        }
    }

    @Test
    public void validateCredit_Ok() {
        Account creditAccount = new Account();
        creditAccount.setId("creditAccount");
        creditAccount.setBalance(new BigDecimal("5000"));
        balanceValidation.validateCredit(creditAccount, new BigDecimal("2000"));
    }

    @Test
    public void validateCreditFail_BalanceOverLimit() {
        Account creditAccount = new Account();
        creditAccount.setId("creditAccount");
        creditAccount.setBalance(new BigDecimal("9000"));
        try {
            balanceValidation.validateCredit(creditAccount, new BigDecimal("2000"));
            fail("Expected but not thrown: WebApplicationException");
        } catch (WebApplicationException expectedException) {
            ApiResponse apiResponse = (ApiResponse) expectedException.getResponse().getEntity();
            assertThat(apiResponse.getMessage()).isEqualTo(format("Transfer failed, credited amount too big to transfer to account:%s", creditAccount.getId()));
        }
    }
}