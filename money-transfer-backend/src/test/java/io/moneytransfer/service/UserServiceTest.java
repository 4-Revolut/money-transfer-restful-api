package io.moneytransfer.service;

import io.moneytransfer.model.Account;
import io.moneytransfer.model.User;
import io.moneytransfer.sampledata.AccountSampleData;
import io.moneytransfer.sampledata.UserSampleData;
import io.moneytransfer.store.InMemoryStore;
import io.moneytransfer.validation.user.UserDuplicateCheck;
import io.moneytransfer.validation.user.UserValidation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    public static final String EXCEPTION_DESCRIPTION = "exception description";

    @InjectMocks
    private UserService userService;

    @Mock
    private UserValidation userValidation;
    @Mock
    private UserDuplicateCheck userDuplicateCheck;
    @Mock
    private UserFetchService userFetchService;

    @Spy
    private InMemoryStore inMemoryStore;
    @Spy
    private AccountService accountService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private UserSampleData userSampleData = new UserSampleData();
    private AccountSampleData accountSampleData = new AccountSampleData();

    @Test
    public void userCreatedOk_withPromoAccount() {
        User userToCreate = userSampleData.userToCreateWithoutAccount();
        User expectedUser = userSampleData.userToCreateWithAccount();

        doCallRealMethod().when(accountService).createAccount(userToCreate);
        doCallRealMethod().when(inMemoryStore).getAllData();
        Response response = userService.addUser(userToCreate);

        User createdUser = (User) response.getEntity();
        assertThat(expectedUser).isEqualToIgnoringGivenFields(createdUser, "id", "accountArray");
        assertThat(accountSampleData.promoAccount()).isEqualToIgnoringGivenFields(createdUser.getAccountArray().get(0), "id");
    }

    @Test
    public void userCreatedOk_withSuppliedAccount() {
        User userToCreate = userSampleData.userToCreateWithAccount();
        User expectedUser = userSampleData.userToCreateWithAccount();

        String accountId = UUID.randomUUID().toString();
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.getAccountArray().get(0).setId(accountId);
            return null;
        }).when(accountService).createAccount(userToCreate);
        doCallRealMethod().when(inMemoryStore).getAllData();

        Response response = userService.addUser(userToCreate);

        User createdUser = (User) response.getEntity();
        assertThat(expectedUser).isEqualToIgnoringGivenFields(createdUser, "id");
    }

    @Test
    public void addUserFailure_userValidationError() {
        User userToCreate = userSampleData.userToCreateWithoutAccount();

        expectedException.expect(WebApplicationException.class);
        expectedException.expectMessage(EXCEPTION_DESCRIPTION);
        doThrow(new WebApplicationException(EXCEPTION_DESCRIPTION)).when(userValidation).validate(userToCreate);

        userService.addUser(userToCreate);
    }

    @Test
    public void addUserFailure_duplicateUser() {
        User userToCreate = userSampleData.userToCreateWithoutAccount();

        expectedException.expect(WebApplicationException.class);
        expectedException.expectMessage(EXCEPTION_DESCRIPTION);
        doThrow(new WebApplicationException(EXCEPTION_DESCRIPTION)).when(userDuplicateCheck).validate(userToCreate);

        userService.addUser(userToCreate);
    }

    @Test
    public void addUserFailure_accountValidationError() {
        User userToCreate = userSampleData.userToCreateWithAccount();

        expectedException.expect(WebApplicationException.class);
        expectedException.expectMessage(EXCEPTION_DESCRIPTION);
        doThrow(new WebApplicationException(EXCEPTION_DESCRIPTION)).when(accountService).createAccount(userToCreate);

        userService.addUser(userToCreate);
    }

    @Test
    public void editUser() {
        String existingUserId = "existingUserId";
        User userToEdit = userSampleData.userToCreateWithAccount();
        userToEdit.setId(existingUserId);
        userToEdit.setFirstname("firstname-edit");
        userToEdit.setLastname("lastname-edit");

        Account userToEditAccount = userToEdit.getAccountArray().get(0);
        userToEditAccount.setId("account-id-edit");
        userToEditAccount.setBalance(new BigDecimal("4321"));
        userToEditAccount.setName("account-name-edit");

        User existingUser = userSampleData.userToCreateWithAccount();
        existingUser.setId(existingUserId);

        when(userFetchService.getUser(userToEdit.getId())).thenReturn(existingUser);
        doCallRealMethod().when(inMemoryStore).getAllData();
        Response response = userService.editUser(userToEdit);
        User updatedUser = (User) response.getEntity();

        User expectedUser = userToEdit;
        existingUser.setAccountArray(existingUser.getAccountArray());

        assertThat(expectedUser).isEqualToIgnoringGivenFields(updatedUser);
    }

    @Test
    public void editUserFail_userValidationError() {
        User userToEdit = userSampleData.userToCreateWithAccount();

        expectedException.expect(WebApplicationException.class);
        expectedException.expectMessage(EXCEPTION_DESCRIPTION);
        doThrow(new WebApplicationException(EXCEPTION_DESCRIPTION)).when(userValidation).validate(userToEdit);

        userService.editUser(userToEdit);
    }

    @Test
    public void editUserFail_userNotExists() {
        User userToEdit = userSampleData.userToCreateWithAccount();

        expectedException.expect(WebApplicationException.class);
        expectedException.expectMessage(EXCEPTION_DESCRIPTION);
        doThrow(new WebApplicationException(EXCEPTION_DESCRIPTION)).when(userFetchService).getUser(userToEdit.getId());

        userService.editUser(userToEdit);
    }

    @Test
    public void editUserFail_duplicateUser() {
        User userToEdit = userSampleData.userToCreateWithAccount();

        expectedException.expect(WebApplicationException.class);
        expectedException.expectMessage(EXCEPTION_DESCRIPTION);
        doThrow(new WebApplicationException(EXCEPTION_DESCRIPTION)).when(userDuplicateCheck).validate(userToEdit);

        userService.editUser(userToEdit);
    }
}