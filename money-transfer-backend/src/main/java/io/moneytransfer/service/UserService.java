package io.moneytransfer.service;

import io.moneytransfer.api.ApiResponse;
import io.moneytransfer.api.ApiResponseMessage;
import io.moneytransfer.model.User;
import io.moneytransfer.store.InMemoryStore;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Set;

import static constants.Constants.ERROR;
import static java.util.UUID.randomUUID;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.status;

public class UserService {

    public UserService() {
    }

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public Response addUser(User user, InMemoryStore inMemoryStore) {
        if (user == null) {
            throw new BadRequestException();
        }
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.size() > 0) {
            return status(BAD_REQUEST)
                    .entity(new ApiResponse(ERROR, violations.stream().findFirst().get().getMessage()))
                    .header(CONTENT_TYPE, APPLICATION_JSON)
                    .build();
        }
        user.setId(randomUUID().toString());
        inMemoryStore.getUsers().put(user.getId(), user);
        return Response.ok().entity(user).build();
    }

    public Response editUser(User userUpdate) {
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "editUser magic!")).build();
    }

    public Response getUserById(String userid) {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "getUserById magic!")).build();
    }
}
