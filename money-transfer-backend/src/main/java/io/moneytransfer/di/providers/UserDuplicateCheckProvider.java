package io.moneytransfer.di.providers;


import io.moneytransfer.validation.user.UserDuplicateCheck;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class UserDuplicateCheckProvider implements ContextResolver<UserDuplicateCheck> {

    private UserDuplicateCheck userDuplicateCheck = new UserDuplicateCheck();

    @Override
    public UserDuplicateCheck getContext(Class klass) {
        return userDuplicateCheck;
    }
}
