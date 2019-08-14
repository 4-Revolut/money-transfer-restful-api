package io.moneytransfer.service;

import io.moneytransfer.model.User;
import io.moneytransfer.store.InMemoryStore;
import io.moneytransfer.validation.user.UserDuplicateCheck;
import io.moneytransfer.validation.user.UserValidation;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import static java.util.UUID.randomUUID;

@Singleton
public class UserService {

    @Inject private UserValidation userValidation;
    @Inject private InMemoryStore inMemoryStore;
    @Inject private AccountService accountService;
    @Inject private UserDuplicateCheck userDuplicateCheck;
    @Inject private UserFetchService userFetchService;

    public UserService() {
    }

    public Response addUser(User user) {
        userValidation.validate(user);
        userDuplicateCheck.validate(user);
        String userId = randomUUID().toString();
        user.setId(userId);
        accountService.createAccount(user);
        inMemoryStore.getAllData().put(user.getId(), user);
        return Response.ok().entity(inMemoryStore.getAllData().get(userId)).build();
    }

    public Response editUser(User userUpdate) {
        userValidation.validate(userUpdate);
        User currentUser = userFetchService.getUser(userUpdate.getId());
        userDuplicateCheck.validate(userUpdate);
        userUpdate.setAccountArray(currentUser.getAccountArray());
        inMemoryStore.getAllData().put(userUpdate.getId(), userUpdate);
        return Response.ok().entity(inMemoryStore.getAllData().get(userUpdate.getId())).build();
    }
}
