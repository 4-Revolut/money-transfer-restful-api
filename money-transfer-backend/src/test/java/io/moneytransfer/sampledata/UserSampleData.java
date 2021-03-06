package io.moneytransfer.sampledata;

import io.moneytransfer.model.Account;
import io.moneytransfer.model.AccountArray;
import io.moneytransfer.model.User;

import java.math.BigDecimal;
import java.util.UUID;

public class UserSampleData {

    AccountSampleData accountSampleData = new AccountSampleData();

    public User userToCreateWithoutAccount() {
        User user = new User();
        user.setEmail("some@email.com");
        user.setFirstname("someFirstName");
        user.setFirstname("someLastName");

        return user;
    }

    public User userToCreateWithAccount() {
        User user = new User();
        user.setEmail("some@email.com");
        user.setFirstname("someFirstName");
        user.setFirstname("someLastName");

        AccountArray accountArray = new AccountArray();
        Account account = new Account();
        account.setBalance(new BigDecimal("2000"));
        account.setName("someAccountName");
        accountArray.add(account);
        user.setAccountArray(accountArray);

        return user;
    }

    public User existingUserWithPromoAccount() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail("some@email.com");
        user.setFirstname("someFirstName");
        user.setFirstname("someLastName");


        AccountArray accountArray = new AccountArray();
        Account account = accountSampleData.promoAccount();
        account.setId(UUID.randomUUID().toString());
        accountArray.add(account);
        user.setAccountArray(accountArray);

        return user;
    }

}
