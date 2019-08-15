package io.moneytransfer.service;

import io.moneytransfer.api.ApiResponse;
import io.moneytransfer.model.User;
import io.moneytransfer.store.InMemoryStore;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static io.moneytransfer.constants.MiscConstants.ERROR_RESPONSE_TYPE;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Dependent
public class UserFetchService {

    @Inject
    private InMemoryStore inMemoryStore;

    public UserFetchService() {
    }

    public Response getUserWeb(String userid) {
        User user = getUser(userid);
        return Response.ok().entity(user).build();
    }

    public User getUser(String userid) {
        return
            inMemoryStore.getUsers()
            .stream()
            .filter(u -> u.getId().equals(userid))
            .findFirst()
            .orElseThrow(() ->
                new WebApplicationException(
                    Response
                        .status(BAD_REQUEST)
                        .header(CONTENT_TYPE, APPLICATION_JSON)
                        .entity(new ApiResponse(ERROR_RESPONSE_TYPE, String.format("User with id:%s does not exist", userid)))
                        .build()));
    }
}
