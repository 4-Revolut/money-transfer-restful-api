package io.moneytransfer.validation.transfer;

import io.moneytransfer.api.ApiResponse;
import io.moneytransfer.model.Transfer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;
import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class TransferValidationTest {

    TransferValidation transferValidation = new TransferValidation();

    @Test
    public void validate_pass() {
        Transfer transfer = new Transfer();
        transfer.setDebitUserId(UUID.randomUUID().toString());
        transfer.setDebitAccountId(UUID.randomUUID().toString());

        transfer.setCreditUserId(UUID.randomUUID().toString());
        transfer.setCreditAccountId(UUID.randomUUID().toString());

        transfer.setAmount(new BigDecimal("1000"));

        transferValidation.validate(transfer);
    }

    @Test
    public void validateFail_transferIsNull() {
        try {
            transferValidation.validate(null);
            fail("Expected but not thrown: WebApplicationException");
        } catch (WebApplicationException expectedException) {
            ApiResponse apiResponse = (ApiResponse) expectedException.getResponse().getEntity();
            assertThat(apiResponse.getMessage()).isEqualTo("Provide transfer details as json entity");
        }
    }

    @Test
    public void validateFail_constraintViolation() {
        Transfer transfer = new Transfer();
        try {
            transferValidation.validate(transfer);
            fail("Expected but not thrown: WebApplicationException");
        } catch (WebApplicationException expectedException) {
            ApiResponse apiResponse = (ApiResponse) expectedException.getResponse().getEntity();
            assertThat(apiResponse.getMessage()).isNotEmpty();
            assertThat(apiResponse.getMessage()).isNotEqualTo("Provide transfer details as json entity");
        }
    }
}