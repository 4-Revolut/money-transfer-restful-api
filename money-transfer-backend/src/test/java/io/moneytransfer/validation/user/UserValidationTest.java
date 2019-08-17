package io.moneytransfer.validation.user;

import io.moneytransfer.api.ApiResponse;
import io.moneytransfer.model.User;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class UserValidationTest {

    private UserValidation userValidation = new UserValidation();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void validate_pass() {
        User user = new User();
        user.setEmail("some@email.com");
        user.setFirstname("firstname");
        user.setLastname("lastname");

        userValidation.validate(user);
    }

    @Test
    public void validateFail_userIsNull() {
        try {
            userValidation.validate(null);
            fail("Expected but not thrown: WebApplicationException");
        } catch (WebApplicationException expectedException) {
            ApiResponse apiResponse = (ApiResponse) expectedException.getResponse().getEntity();
            assertThat(apiResponse.getMessage()).isEqualTo("Provide new User information as json");
        }
    }

    @Test
    public void validateFail_constraintViolation() {

        User user = new User();

        try {
            userValidation.validate(user);
            fail("Expected but not thrown: WebApplicationException");
        } catch (WebApplicationException expectedException) {
            ApiResponse apiResponse = (ApiResponse) expectedException.getResponse().getEntity();
            assertThat(apiResponse.getMessage()).isNotEmpty();
            assertThat(apiResponse.getMessage()).isNotEqualTo("Provide new User information as json");
        }
    }
}