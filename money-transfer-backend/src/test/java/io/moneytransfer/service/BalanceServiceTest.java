package io.moneytransfer.service;

import io.moneytransfer.model.Account;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class BalanceServiceTest {

    private BalanceService balanceService = new BalanceService();

    @Test
    public void settle() {

        Account debitAccount = new Account(null, "debit-account", new BigDecimal("1000"));
        Account creditAccount = new Account(null, "debit-account", new BigDecimal("1000"));

        BigDecimal transferAmount = new BigDecimal("300");

        balanceService.settle(debitAccount, creditAccount, transferAmount);

        Assertions.assertThat(debitAccount.getBalance()).isEqualByComparingTo(new BigDecimal("700"));
        Assertions.assertThat(creditAccount.getBalance()).isEqualByComparingTo(new BigDecimal("1300"));
    }
}