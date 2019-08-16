package io.moneytransfer.service;

import io.moneytransfer.model.Account;
import io.moneytransfer.model.User;
import io.moneytransfer.sampledata.AccountSampleData;
import io.moneytransfer.sampledata.UserSampleData;
import io.moneytransfer.validation.account.AccountValidation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    public static final String EXCEPTION_DESCRIPTION = "exception description";
    public static final int UUID_LENGTH = 36;

    @InjectMocks private AccountService accountService;

    @Mock private AccountValidation accountValidation;
    @Mock private UserFetchService userFetchService;


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private UserSampleData userSampleData = new UserSampleData();
    private AccountSampleData accountSampleData = new AccountSampleData();


    @Test
    public void createPromoAccountOk() {
        User user = userSampleData.userToCreateWithoutAccount();

        accountService.createAccount(user);

        Account promoAccount = accountSampleData.promoAccount();

        assertThat(promoAccount).isEqualToIgnoringGivenFields(user.getAccountArray().get(0), "id");
        assertThat(user.getAccountArray().get(0).getId().length()).isEqualTo(UUID_LENGTH);
    }

    @Test
    public void createSuppliedAccountOk() {
        User user = userSampleData.userToCreateWithAccount();

        String accountIdToBeOverridden = "account-id-expected-to-be-overridden";
        Account expectedAccount = user.getAccountArray().get(0);
        expectedAccount.setId(accountIdToBeOverridden);


        accountService.createAccount(user);

        assertThat(expectedAccount).isEqualToIgnoringGivenFields(user.getAccountArray().get(0), "id");
        assertThat(user.getAccountArray().get(0).getId()).isNotEqualTo(accountIdToBeOverridden);
        assertThat(user.getAccountArray().get(0).getId().length()).isEqualTo(UUID_LENGTH);
    }


    @Test
    public void createAccountFail_accountValidationFailed() {
        User user = userSampleData.userToCreateWithAccount();

        expectedException.expect(WebApplicationException .class);
        expectedException.expectMessage(EXCEPTION_DESCRIPTION);
        doThrow(new WebApplicationException(EXCEPTION_DESCRIPTION)).when(accountValidation).validate(user);

        accountService.createAccount(user);
    }

    @Test
    public void getAccountOk() {
        User user = userSampleData.userToCreateWithAccount();
        user.setId("someUserId");
        Account expectedAccount = user.getAccountArray().get(0);
        expectedAccount.setId("someAccountId");

        when(userFetchService.getUser(user.getId())).thenReturn(user);
        Account fetchedAccount = accountService.getAccount(user.getId(), expectedAccount.getId());
        assertThat(expectedAccount).isEqualTo(fetchedAccount);
    }

    @Test
    public void getAccountFailed_userNotFound() {
        User user = userSampleData.userToCreateWithAccount();
        user.setId("someUserId");

        Account expectedAccount = user.getAccountArray().get(0);
        expectedAccount.setId("someAccountId");

        expectedException.expect(WebApplicationException.class);
        expectedException.expectMessage(EXCEPTION_DESCRIPTION);
        doThrow(new WebApplicationException(EXCEPTION_DESCRIPTION)).when(userFetchService).getUser(user.getId());

        accountService.getAccount(user.getId(), expectedAccount.getId());
    }

    @Test
    public void getAccountFailed_accountNotFound() {
        User user = userSampleData.userToCreateWithAccount();
        user.setId("someUserId");
        user.getAccountArray().get(0).setId("non-existing-account-id");

        Account expectedAccount = userSampleData.userToCreateWithAccount().getAccountArray().get(0);
        expectedAccount.setId("someAccountId");

        when(userFetchService.getUser(user.getId())).thenReturn(user);

        expectedException.expect(WebApplicationException.class);
        expectedException.expectMessage("HTTP 400 Bad Request");

        accountService.getAccount(user.getId(), expectedAccount.getId());
    }
}