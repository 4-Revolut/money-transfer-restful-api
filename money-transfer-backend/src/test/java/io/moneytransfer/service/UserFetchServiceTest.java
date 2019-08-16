package io.moneytransfer.service;

import io.moneytransfer.model.User;
import io.moneytransfer.sampledata.UserSampleData;
import io.moneytransfer.store.InMemoryStore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserFetchServiceTest {

    @InjectMocks
    private UserFetchService userFetchService;

    @Mock private InMemoryStore inMemoryStore;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private UserSampleData userSampleData = new UserSampleData();


    @Test
    public void getUserWebOk() {

        User someUser = userSampleData.userToCreateWithAccount();
        someUser.setId(UUID.randomUUID().toString());

        User expectedUser = userSampleData.userToCreateWithAccount();
        expectedUser.setEmail("expectedUser@email.com");
        expectedUser.setId(UUID.randomUUID().toString());
        expectedUser.setFirstname("first-name-expected-user");
        expectedUser.setFirstname("last-name-expected-user");
        expectedUser.setFirstname("last-name-expected-user");

        Collection<User> users = new HashSet<>();
        users.add(someUser);
        users.add(expectedUser);

        when(inMemoryStore.getUsers()).thenReturn(users);

        Response response = userFetchService.getUserWeb(expectedUser.getId());
        User fetchedUser = (User) response.getEntity();

        assertThat(expectedUser).isEqualTo(fetchedUser);
    }

    @Test
    public void getUserOk() {
        User someUser = userSampleData.userToCreateWithAccount();
        someUser.setId(UUID.randomUUID().toString());

        User expectedUser = userSampleData.userToCreateWithAccount();
        expectedUser.setEmail("expectedUser@email.com");
        expectedUser.setId(UUID.randomUUID().toString());
        expectedUser.setFirstname("first-name-expected-user");
        expectedUser.setFirstname("last-name-expected-user");
        expectedUser.setFirstname("last-name-expected-user");

        Collection<User> users = new HashSet<>();
        users.add(someUser);
        users.add(expectedUser);

        when(inMemoryStore.getUsers()).thenReturn(users);

        User fetchedUser = userFetchService.getUser(expectedUser.getId());

        assertThat(expectedUser).isEqualTo(fetchedUser);
    }


    @Test
    public void getUserFailure_userNotExist() {
        User someUser = userSampleData.userToCreateWithAccount();
        someUser.setId(UUID.randomUUID().toString());

        User expectedUser = userSampleData.userToCreateWithAccount();
        expectedUser.setEmail("expectedUser@email.com");
        expectedUser.setId(UUID.randomUUID().toString());
        expectedUser.setFirstname("first-name-other-user");
        expectedUser.setFirstname("last-name-other-user");
        expectedUser.setFirstname("last-name-other-user");

        Collection<User> users = new HashSet<>();
        users.add(someUser);
        users.add(expectedUser);

        when(inMemoryStore.getUsers()).thenReturn(users);

        expectedException.expect(WebApplicationException.class);
        expectedException.expectMessage("HTTP 400 Bad Request");

        User fetchedUser = userFetchService.getUser("non-existing-user-id");

        assertThat(expectedUser).isEqualTo(fetchedUser);
    }
}