package io.moneytransfer.validation.transfer;

import io.moneytransfer.api.ApiResponse;
import io.moneytransfer.model.Account;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import java.math.BigDecimal;

import static io.moneytransfer.constants.MiscConstants.ERROR_RESPONSE_TYPE;
import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.status;

@Singleton
public class BalanceValidation {

    public void validateDebit(Account debitAccount, BigDecimal debitAmount) {
        if (debitAccount.getBalance().subtract(debitAmount).compareTo(ZERO) < 0) {
            throw new WebApplicationException(
                    status(BAD_REQUEST)
                            .entity(new ApiResponse(ERROR_RESPONSE_TYPE, format("Account:%s does not have enough funds to make transfer", debitAccount.getId())))
                            .header(CONTENT_TYPE, APPLICATION_JSON)
                            .build());
        }
    }

    public void validateCredit(Account creditAccount, BigDecimal creditAmount) {
        if (creditAccount.getBalance().add(creditAmount).compareTo(ZERO) > 10000) {
            throw new WebApplicationException(
                    status(BAD_REQUEST)
                            .entity(new ApiResponse(ERROR_RESPONSE_TYPE, format("Transfer failed, credited amount too big to transfer to account:%s", creditAccount.getId())))
                            .header(CONTENT_TYPE, APPLICATION_JSON)
                            .build());
        }
    }
}
