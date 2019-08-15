package io.moneytransfer.config;

import io.moneytransfer.service.*;
import io.moneytransfer.store.InMemoryStore;
import io.moneytransfer.validation.account.AccountValidation;
import io.moneytransfer.validation.transfer.BalanceValidation;
import io.moneytransfer.validation.transfer.TransferValidation;
import io.moneytransfer.validation.user.UserDuplicateCheck;
import io.moneytransfer.validation.user.UserValidation;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class CdiBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(UserService.class).to(UserService.class);
        bind(UserFetchService.class).to(UserFetchService.class);
        bind(UserValidation.class).to(UserValidation.class);
        bind(InMemoryStore.class).to(InMemoryStore.class).in(Singleton.class);
        bind(AccountService.class).to(AccountService.class);
        bind(AccountValidation.class).to(AccountValidation.class);
        bind(UserDuplicateCheck.class).to(UserDuplicateCheck.class);
        bind(TransferService.class).to(TransferService.class);
        bind(TransferValidation.class).to(TransferValidation.class);
        bind(BalanceService.class).to(BalanceService.class);
        bind(BalanceValidation.class).to(BalanceValidation.class);
    }
}
