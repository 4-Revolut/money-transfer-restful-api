package io.moneytransfer.validation.user;

import io.moneytransfer.api.ApiResponse;
import io.moneytransfer.model.User;

import javax.annotation.PostConstruct;
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
public class UserValidation {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @PostConstruct
    public void afterCreate() {
        System.out.println("after create");
    }

    public void validate(User user) {
        if (user == null) {
            throw new WebApplicationException(
                    Response
                            .status(BAD_REQUEST)
                            .header(CONTENT_TYPE, APPLICATION_JSON)
                            .entity(new ApiResponse(ERROR_RESPONSE_TYPE, "Provide new User information as json"))
                            .build());
        }
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.size() > 0) {
            throw new WebApplicationException(
                    status(BAD_REQUEST)
                            .entity(new ApiResponse(ERROR_RESPONSE_TYPE, violations.stream().findFirst().get().getMessage()))
                            .header(CONTENT_TYPE, APPLICATION_JSON)
                            .build());
        }

    }

}
