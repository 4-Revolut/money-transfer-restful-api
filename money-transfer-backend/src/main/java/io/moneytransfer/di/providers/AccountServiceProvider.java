package io.moneytransfer.di.providers;


import io.moneytransfer.service.AccountService;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class AccountServiceProvider implements ContextResolver<AccountService> {

    private AccountService accountService = new AccountService();

    @Override
    public AccountService getContext(Class klass) {
        return accountService;
    }
}
