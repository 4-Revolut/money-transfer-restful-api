package io.moneytransfer.di.providers;


import io.moneytransfer.validation.user.UserValidation;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class UserValidationProvider implements ContextResolver<UserValidation> {

    private UserValidation userValidation = new UserValidation();

    @Override
    public UserValidation getContext(Class klass) {
        return userValidation;
    }
}
