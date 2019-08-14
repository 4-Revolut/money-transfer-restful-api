package io.moneytransfer.validation.account;

import io.moneytransfer.api.ApiResponse;
import io.moneytransfer.model.Account;
import io.moneytransfer.model.User;

import javax.inject.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Set;

import static io.moneytransfer.constants.MiscConstants.ERROR_RESPONSE_TYPE;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.status;

@Singleton
public class AccountValidation {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public void validate(User user) {
        if (user.getAccountArray().size() > 1) {
            throw new WebApplicationException(
                    Response
                            .status(BAD_REQUEST)
                            .header(CONTENT_TYPE, APPLICATION_JSON)
                            .entity(new ApiResponse(ERROR_RESPONSE_TYPE, "User can be created with one account only"))
                            .build());
        }
        Set<ConstraintViolation<Account>> violations = validator.validate(user.getAccountArray().get(0));
        if (violations.size() > 0) {
            throw new WebApplicationException(
                    status(BAD_REQUEST)
                            .entity(new ApiResponse(ERROR_RESPONSE_TYPE, violations.stream().findFirst().get().getMessage()))
                            .header(CONTENT_TYPE, APPLICATION_JSON)
                            .build());
        }

    }

}
