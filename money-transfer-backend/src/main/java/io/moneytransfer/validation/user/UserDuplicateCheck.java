package io.moneytransfer.validation.user;

import io.moneytransfer.api.ApiResponse;
import io.moneytransfer.model.User;
import io.moneytransfer.store.InMemoryStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.function.Consumer;

import static io.moneytransfer.constants.MiscConstants.ERROR_RESPONSE_TYPE;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CONFLICT;

@Singleton
public class UserDuplicateCheck {

    @Inject private InMemoryStore inMemoryStore;

    Consumer<User> duplicateFound = user -> {
        throw new WebApplicationException(
                Response
                        .status(CONFLICT)
                        .header(CONTENT_TYPE, APPLICATION_JSON)
                        .entity(new ApiResponse(ERROR_RESPONSE_TYPE, "User with provided email already exists"))
                        .build());
    };

    public void validate(User newUser) {
        inMemoryStore.getAllData().values().stream().filter(user -> user.getEmail().equals(newUser.getEmail())).findFirst().ifPresent(duplicateFound);
    }

    public void setInMemoryStore(InMemoryStore inMemoryStore) {
        this.inMemoryStore = inMemoryStore;
    }


}
