package io.moneytransfer.sampledata;

import io.moneytransfer.model.Account;

import java.math.BigDecimal;

public class AccountSampleData {

    public Account promoAccount() {
        return new Account(null, "promo-account", new BigDecimal("1000"));
    }

}
