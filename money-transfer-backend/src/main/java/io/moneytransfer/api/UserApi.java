package io.moneytransfer.api;

import io.moneytransfer.model.User;
import io.moneytransfer.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserApi {

//    @Context
//    javax.ws.rs.ext.Providers providers;

    @Inject
    private UserService userService;

    @POST
    @Consumes({"application/json"})
    @Operation(summary = "creates new user", description = "", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user created"),
            @ApiResponse(responseCode = "400", description = "invalid input"),
            @ApiResponse(responseCode = "409", description = "user already exists")})
    public Response addUser(@Parameter(description = "create new user") User user) {
        return userService.addUser(user);
    }

    @PUT
    @Consumes({"application/json"})
    @Operation(summary = "updates user", description = "", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user updated"),
            @ApiResponse(responseCode = "400", description = "invalid input"),
            @ApiResponse(responseCode = "404", description = "user not found")})
    public Response editUser(@Parameter(description = "edit existing user") User userUpdate) {
        return userService.editUser(userUpdate);
    }

    @GET
    @Path("/{userid}")
    @Produces({"application/json"})
    @Operation(summary = "Get user by user id", description = "", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user found", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "user not found")})
    public Response getUserById(@Parameter(description = "user id that needs to be fetched", required = true) @PathParam("userid") String userid) {
        return userService.getUserById(userid);
    }
}
