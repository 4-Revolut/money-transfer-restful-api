package io.moneytransfer.validation.user;

import io.moneytransfer.api.ApiResponse;
import io.moneytransfer.model.User;
import io.moneytransfer.sampledata.UserSampleData;
import io.moneytransfer.store.InMemoryStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;
import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDuplicateCheckTest {

    @Mock
    private InMemoryStore inMemoryStore;

    @InjectMocks
    private UserDuplicateCheck userDuplicateCheck;

    private UserSampleData userSampleData = new UserSampleData();

    @Test
    public void validatePass_userWithDifferentEmail() {
        User newUser = userSampleData.userToCreateWithAccount();
        User someUser = userSampleData.userToCreateWithAccount();
        someUser.setEmail("someuser@email.com");
        User otherUser = userSampleData.userToCreateWithAccount();
        otherUser.setEmail("otheruser@email.com");
        Collection users = new HashSet();
        users.add(otherUser);
        users.add(someUser);
        when(inMemoryStore.getUsers()).thenReturn(users);
        userDuplicateCheck.validate(newUser);
    }

    @Test
    public void validatePass_userWithSameEmailSameId() {
        String sameUserId = "someUserId";
        String duplicateUserEmail = "duplicate@email.com";
        User newUser = userSampleData.userToCreateWithAccount();
        newUser.setId(sameUserId);
        newUser.setEmail(duplicateUserEmail);

        User someUser = userSampleData.userToCreateWithAccount();
        someUser.setEmail(duplicateUserEmail);
        someUser.setId(sameUserId);

        User otherUser = userSampleData.userToCreateWithAccount();
        otherUser.setEmail("otheruser@email.com");
        otherUser.setId("otherUserId");

        Collection users = new HashSet();
        users.add(otherUser);
        users.add(someUser);

        userDuplicateCheck.validate(newUser);
    }

    @Test
    public void validateFail_duplicateUser() {
        String duplicateUserEmail = "duplicate@email.com";
        User newUser = userSampleData.userToCreateWithAccount();
        newUser.setId("newUserId");
        newUser.setEmail(duplicateUserEmail);

        User someUser = userSampleData.userToCreateWithAccount();
        someUser.setEmail(duplicateUserEmail);
        someUser.setId("someUserId");

        User otherUser = userSampleData.userToCreateWithAccount();
        otherUser.setEmail("otheruser@email.com");
        otherUser.setId("otherUserId");

        Collection users = new HashSet();
        users.add(otherUser);
        users.add(someUser);

        when(inMemoryStore.getUsers()).thenReturn(users);

        try {
            userDuplicateCheck.validate(newUser);
            fail("Expected but not thrown: WebApplicationException");
        } catch (WebApplicationException expectedException) {
            ApiResponse apiResponse = (ApiResponse) expectedException.getResponse().getEntity();
            assertThat(apiResponse.getMessage()).isEqualTo("User with provided email already exists");
        }
    }
}