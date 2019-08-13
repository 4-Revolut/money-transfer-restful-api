package io.moneytransfer.api;


import io.moneytransfer.store.InMemoryStore;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/test")
public class TestContextApi {

    @Inject
    private InMemoryStore inMemoryStore;

    @Path("/clearContext")
    @GET
    public Response clearContext() {
        inMemoryStore.getUsers().clear();
        return Response.ok().build();
    }
}
