package io.moneytransfer.service;

import io.moneytransfer.model.Account;

import javax.inject.Singleton;
import java.math.BigDecimal;

@Singleton
public class BalanceService {


    public void settle(Account debitAccount, Account creditAccount, BigDecimal amount) {
        debitAccount.setBalance(debitAccount.getBalance().subtract(amount));
        creditAccount.setBalance(creditAccount.getBalance().add(amount));
    }
}
