package io.moneytransfer.service;

import io.moneytransfer.api.ApiResponseMessage;
import io.moneytransfer.model.AccountArray;
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

    public UserService() {
    }

    public Response addUser(User user) {
        userValidation.validate(user);
        userDuplicateCheck.setInMemoryStore(inMemoryStore);
        userDuplicateCheck.validate(user);
        String userId = randomUUID().toString();
        user.setId(userId);
        if (user.getAccountArray() == null || user.getAccountArray().isEmpty()) {
            AccountArray accountArray = new AccountArray();
            accountArray.add(accountService.createPromoAccount());
            user.setAccountArray(accountArray);
        }
        inMemoryStore.getUsers().put(user.getId(), user);
        return Response.ok().entity(inMemoryStore.getUsers().get(userId)).build();
    }

    public Response editUser(User userUpdate) {
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "editUser magic!")).build();
    }

    public Response getUserById(String userid) {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "getUserById magic!")).build();
    }

    public void setUserValidation(UserValidation userValidation) {
        this.userValidation = userValidation;
    }

    public void setInMemoryStore(InMemoryStore inMemoryStore) {
        this.inMemoryStore = inMemoryStore;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setUserDuplicateCheck(UserDuplicateCheck userDuplicateCheck) {
        this.userDuplicateCheck = userDuplicateCheck;
    }
}
