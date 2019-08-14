package io.moneytransfer.service;

import io.moneytransfer.api.ApiResponse;
import io.moneytransfer.model.Account;
import io.moneytransfer.model.AccountArray;
import io.moneytransfer.model.User;
import io.moneytransfer.validation.account.AccountValidation;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.UUID;

import static io.moneytransfer.constants.MiscConstants.ERROR_RESPONSE_TYPE;
import static java.lang.String.format;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Singleton
public class AccountService {

    public static final String PROMO_ACCOUNT = "promo-account";
    public static final String THOUSAND = "1000";

    @Inject private AccountValidation accountValidation;
    @Inject private UserFetchService userFetchService;

    public void createAccount(User user) {
        if (user.getAccountArray() == null || user.getAccountArray().isEmpty()) {
            AccountArray accountArray = new AccountArray();
            accountArray.add(createPromoAccount());
            user.setAccountArray(accountArray);
        } else {
            accountValidation.validate(user);
            user.getAccountArray().get(0).setId(UUID.randomUUID().toString());
        }
    }

    public Account createPromoAccount() {
        return new Account(UUID.randomUUID().toString(), PROMO_ACCOUNT, new BigDecimal(THOUSAND));
    }

    public Account getAccount(String userid, String accountid) {
        return
                userFetchService.getUser(userid).getAccountArray()
                        .stream()
                        .filter(a -> a.getId().equals(accountid))
                        .findFirst()
                        .orElseThrow(() ->
                                new WebApplicationException(
                                        Response
                                                .status(BAD_REQUEST)
                                                .header(CONTENT_TYPE, APPLICATION_JSON)
                                                .entity(new ApiResponse(ERROR_RESPONSE_TYPE, format("Account with id:%s does not exist for user:%s", accountid, userid)))
                                                .build()));
    }

}
