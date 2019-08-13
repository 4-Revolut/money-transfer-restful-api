package io.moneytransfer.config;

import io.moneytransfer.service.AccountService;
import io.moneytransfer.service.UserService;
import io.moneytransfer.store.InMemoryStore;
import io.moneytransfer.validation.user.UserDuplicateCheck;
import io.moneytransfer.validation.user.UserValidation;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class CdiBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(UserService.class).to(UserService.class).in(Singleton.class);
        bind(UserValidation.class).to(UserValidation.class).in(Singleton.class);
        bind(InMemoryStore.class).to(InMemoryStore.class).in(Singleton.class);
        bind(AccountService.class).to(AccountService.class).in(Singleton.class);
        bind(UserDuplicateCheck.class).to(UserDuplicateCheck.class).in(Singleton.class);
    }
}
