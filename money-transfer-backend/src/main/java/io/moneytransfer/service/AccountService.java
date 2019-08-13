package io.moneytransfer.service;

import io.moneytransfer.model.Account;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.UUID;

@Singleton
public class AccountService {

    public static final String PROMO_ACCOUNT = "promo-account";
    public static final String THOUSAND = "1000";

    public Account createPromoAccount() {
        return new Account(UUID.randomUUID().toString(), PROMO_ACCOUNT, new BigDecimal(THOUSAND));
    }

}
