package io.moneytransfer.service;

import io.moneytransfer.model.Account;
import io.moneytransfer.model.Transfer;
import io.moneytransfer.model.User;
import io.moneytransfer.sampledata.UserSampleData;
import io.moneytransfer.validation.transfer.BalanceValidation;
import io.moneytransfer.validation.transfer.TransferValidation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransferServiceTest {

    public static final String EXCEPTION_DESCRIPTION = "exception description";

    @InjectMocks
    private TransferService transferService;

    @Mock private TransferValidation transferValidation;
    @Mock private UserFetchService userFetchService;
    @Mock private AccountService accountService;
    @Mock private BalanceValidation balanceValidation;

    @Spy private BalanceService balanceService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private UserSampleData userSampleData = new UserSampleData();

    @Test
    public void transferOk() {
        User debitUser = userSampleData.existingUserWithPromoAccount();
        User creditUser = userSampleData.existingUserWithPromoAccount();
        Account debitAccount = debitUser.getAccountArray().get(0);
        Account creditAccount = creditUser.getAccountArray().get(0);

        BigDecimal transferAmount = new BigDecimal("400");
        Transfer transfer = new Transfer();
        transfer.amount(transferAmount);
        transfer.setDebitUserId(debitUser.getId());
        transfer.setDebitAccountId(debitAccount.getId());

        transfer.setCreditUserId(creditUser.getId());
        transfer.setCreditAccountId(creditUser.getAccountArray().get(0).getId());

        when(userFetchService.getUser(debitUser.getId())).thenReturn(debitUser);
        when(userFetchService.getUser(creditUser.getId())).thenReturn(creditUser);
        when(accountService.getAccount(debitUser.getId(), debitAccount.getId())).thenReturn(debitAccount);
        when(accountService.getAccount(creditUser.getId(), creditAccount.getId())).thenReturn(creditAccount);
        doCallRealMethod().when(balanceService).settle(debitAccount, creditAccount, transferAmount);

        transferService.transaction(transfer);

        assertThat(debitAccount.getBalance()).isEqualTo(new BigDecimal("600"));
        assertThat(creditAccount.getBalance()).isEqualTo(new BigDecimal("1400"));
    }

    @Test
    public void transferFail_transferValidationFailed() {
        User debitUser = userSampleData.existingUserWithPromoAccount();
        User creditUser = userSampleData.existingUserWithPromoAccount();
        Transfer transfer = new Transfer();
        transfer.amount(new BigDecimal("400"));
        transfer.setDebitUserId(debitUser.getId());
        transfer.setDebitAccountId(debitUser.getAccountArray().get(0).getId());

        transfer.setCreditUserId(creditUser.getId());
        transfer.setCreditAccountId(creditUser.getAccountArray().get(0).getId());

        expectedException.expect(WebApplicationException.class);
        expectedException.expectMessage(EXCEPTION_DESCRIPTION);
        doThrow(new WebApplicationException(EXCEPTION_DESCRIPTION)).when(transferValidation).validate(transfer);

        transferService.transaction(transfer);
    }

    @Test
    public void transferFail_debitUserNotFound() {
        User debitUser = userSampleData.existingUserWithPromoAccount();
        User creditUser = userSampleData.existingUserWithPromoAccount();
        Transfer transfer = new Transfer();
        transfer.amount(new BigDecimal("400"));
        transfer.setDebitUserId(debitUser.getId());
        transfer.setDebitAccountId(debitUser.getAccountArray().get(0).getId());

        transfer.setCreditUserId(creditUser.getId());
        transfer.setCreditAccountId(creditUser.getAccountArray().get(0).getId());

        expectedException.expect(WebApplicationException.class);
        expectedException.expectMessage(EXCEPTION_DESCRIPTION);
        doThrow(new WebApplicationException(EXCEPTION_DESCRIPTION)).when(userFetchService).getUser(debitUser.getId());

        transferService.transaction(transfer);
    }

    @Test
    public void transferFail_debitAccountNotFound() {
        User debitUser = userSampleData.existingUserWithPromoAccount();
        User creditUser = userSampleData.existingUserWithPromoAccount();
        Account debitAccount = debitUser.getAccountArray().get(0);

        Transfer transfer = new Transfer();
        transfer.amount(new BigDecimal("400"));
        transfer.setDebitUserId(debitUser.getId());
        transfer.setDebitAccountId(debitAccount.getId());

        transfer.setCreditUserId(creditUser.getId());
        transfer.setCreditAccountId(creditUser.getAccountArray().get(0).getId());

        when(userFetchService.getUser(debitUser.getId())).thenReturn(debitUser);
        expectedException.expect(WebApplicationException.class);
        expectedException.expectMessage(EXCEPTION_DESCRIPTION);
        doThrow(new WebApplicationException(EXCEPTION_DESCRIPTION)).when(accountService).getAccount(debitUser.getId(), debitAccount.getId());

        transferService.transaction(transfer);
    }

    @Test
    public void transferFail_debitBalanceValidationFailed() {
        User debitUser = userSampleData.existingUserWithPromoAccount();
        User creditUser = userSampleData.existingUserWithPromoAccount();
        Account debitAccount = debitUser.getAccountArray().get(0);

        BigDecimal transferAmount = new BigDecimal("400");
        Transfer transfer = new Transfer();
        transfer.amount(transferAmount);
        transfer.setDebitUserId(debitUser.getId());
        transfer.setDebitAccountId(debitAccount.getId());

        transfer.setCreditUserId(creditUser.getId());
        transfer.setCreditAccountId(creditUser.getAccountArray().get(0).getId());

        when(userFetchService.getUser(debitUser.getId())).thenReturn(debitUser);
        when(accountService.getAccount(debitUser.getId(), debitAccount.getId())).thenReturn(debitAccount);
        expectedException.expect(WebApplicationException.class);
        expectedException.expectMessage(EXCEPTION_DESCRIPTION);
        doThrow(new WebApplicationException(EXCEPTION_DESCRIPTION)).when(balanceValidation).validateDebit(debitAccount, transferAmount);

        transferService.transaction(transfer);
    }

    @Test
    public void transferFail_creditUserNotFound() {
        User debitUser = userSampleData.existingUserWithPromoAccount();
        User creditUser = userSampleData.existingUserWithPromoAccount();
        Transfer transfer = new Transfer();
        transfer.amount(new BigDecimal("400"));
        transfer.setDebitUserId(debitUser.getId());
        transfer.setDebitAccountId(debitUser.getAccountArray().get(0).getId());

        transfer.setCreditUserId(creditUser.getId());
        transfer.setCreditAccountId(creditUser.getAccountArray().get(0).getId());

        when(userFetchService.getUser(debitUser.getId())).thenReturn(debitUser);
        expectedException.expect(WebApplicationException.class);
        expectedException.expectMessage(EXCEPTION_DESCRIPTION);
        doThrow(new WebApplicationException(EXCEPTION_DESCRIPTION)).when(userFetchService).getUser(creditUser.getId());

        transferService.transaction(transfer);
    }

    @Test
    public void transferFail_creditAccountNotFound() {
        User debitUser = userSampleData.existingUserWithPromoAccount();
        User creditUser = userSampleData.existingUserWithPromoAccount();
        Account debitAccount = debitUser.getAccountArray().get(0);
        Account creditAccount = creditUser.getAccountArray().get(0);

        Transfer transfer = new Transfer();
        transfer.amount(new BigDecimal("400"));
        transfer.setDebitUserId(debitUser.getId());
        transfer.setDebitAccountId(debitAccount.getId());

        transfer.setCreditUserId(creditUser.getId());
        transfer.setCreditAccountId(creditUser.getAccountArray().get(0).getId());

        when(userFetchService.getUser(debitUser.getId())).thenReturn(debitUser);
        when(userFetchService.getUser(creditUser.getId())).thenReturn(creditUser);
        expectedException.expect(WebApplicationException.class);
        expectedException.expectMessage(EXCEPTION_DESCRIPTION);
        doThrow(new WebApplicationException(EXCEPTION_DESCRIPTION)).when(accountService).getAccount(creditUser.getId(), creditAccount.getId());

        transferService.transaction(transfer);
    }

    @Test
    public void transferFail_creditBalanceValidationFailed() {
        User debitUser = userSampleData.existingUserWithPromoAccount();
        User creditUser = userSampleData.existingUserWithPromoAccount();
        Account debitAccount = debitUser.getAccountArray().get(0);
        Account creditAccount = creditUser.getAccountArray().get(0);

        BigDecimal transferAmount = new BigDecimal("400");
        Transfer transfer = new Transfer();
        transfer.amount(transferAmount);
        transfer.setDebitUserId(debitUser.getId());
        transfer.setDebitAccountId(debitAccount.getId());

        transfer.setCreditUserId(creditUser.getId());
        transfer.setCreditAccountId(creditUser.getAccountArray().get(0).getId());

        when(userFetchService.getUser(debitUser.getId())).thenReturn(debitUser);
        when(userFetchService.getUser(creditUser.getId())).thenReturn(creditUser);
        when(accountService.getAccount(creditUser.getId(), creditAccount.getId())).thenReturn(creditAccount);
        expectedException.expect(WebApplicationException.class);
        expectedException.expectMessage(EXCEPTION_DESCRIPTION);
        doThrow(new WebApplicationException(EXCEPTION_DESCRIPTION)).when(balanceValidation).validateCredit(creditAccount, transferAmount);

        transferService.transaction(transfer);
    }
}